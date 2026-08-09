package com.thewarior73.soultether.mixin;

import com.thewarior73.soultether.Blocks.SoulChestBlockEntity;
import com.thewarior73.soultether.Items.SoulTetherItem;
import com.thewarior73.soultether.SoulTether;
import com.thewarior73.soultether.TetherLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Unique
    private static Identifier TetherBreakID = SoulTether.id("tether_breaks");
    @Unique
    private static final Holder.Reference<SoundEvent> TETHER_BREAK = Registry.registerForHolder(
            BuiltInRegistries.SOUND_EVENT,
            TetherBreakID,
            SoundEvent.createVariableRangeEvent(TetherBreakID)
    );
    @Inject(method = "dropEquipment", at = @At("HEAD"))
    private void onDropEquipment(final ServerLevel level, CallbackInfo ci) {
        Player player = (Player) (Object) this;

        // Skip if keepInventory is active
        if (level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
            return;
        }

        // Search for a Soul Tether in inventory
        ItemStack tetherStack = ItemStack.EMPTY;
        int tetherSlot = -1;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof SoulTetherItem) {
                tetherStack = stack;

                SoulTether.LOGGER.debug("SOUL TETHER ITEM FOUND, Stack Id: {}, Is linked ? ({})", i, SoulTetherItem.isLinked(tetherStack));

                if (SoulTetherItem.isLinked(tetherStack)) {
                    tetherSlot = i;
                    break; // Use the first linked tether found
                }
            }
        }

        // If no valid tether item found, skip
        if (tetherSlot == -1) {
            return;
        }

        CustomData customData = tetherStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }

        CompoundTag nbt = customData.copyTag();

        if (!TetherLocation.hasLocationData(nbt)) {
            return;
        }

        Optional<TetherLocation> optLocation = TetherLocation.readNbtData(nbt);

        SoulTether.LOGGER.debug(optLocation.toString());

        if (optLocation.isEmpty()) {
            return;
        }

        BlockPos targetPos = optLocation.get().pos();
        String dimString = optLocation.get().dimension();

        Identifier dimIdentifier = Identifier.tryParse(dimString);
        if (dimIdentifier == null) {
            return;
        }

        ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, dimIdentifier);
        if (dimKey == null) {
            return;
        }

        MinecraftServer server = level.getServer();
        if (server == null) {
            return;
        }

        ServerLevel targetLevel = server.getLevel(dimKey);
        if (targetLevel == null) {
            return;
        }

        // Get or load the chunk at the target position to find the block entity
        targetLevel.getChunkAt(targetPos);
        BlockEntity blockEntity = targetLevel.getBlockEntity(targetPos);
        if (!(blockEntity instanceof SoulChestBlockEntity soulChest)) {
            return;
        }

        if (soulChest instanceof com.thewarior73.soultether.Blocks.SecureSoulChestBlockEntity secureChest) {
            if (secureChest.hasOwner() && !secureChest.isOwner(player)) {
                return;
            }
        }

        SoulTetherItem tetherItem = (SoulTetherItem) tetherStack.getItem();
        com.thewarior73.soultether.config.ModConfig.TetherTier tier = tetherItem.getTier();

        // Calculate durability penalty
        ResourceKey<Level> currentDim = player.level().dimension();
        int durabilityCost = 1;
        if (!currentDim.equals(dimKey)) {
            durabilityCost = (int) Math.ceil(tier.dimensionalCostMultiplier());
        }

        // Damage the tether stack
        int newDamage = tetherStack.getDamageValue() + durabilityCost;
        boolean tetherBroke = false;
        if (newDamage >= tetherStack.getMaxDamage()) {
            tetherBroke = true;
            if (soulChest instanceof com.thewarior73.soultether.Blocks.SecureSoulChestBlockEntity secureChest) {
                secureChest.setLinked(false);
            }
            targetLevel.playSound(null, BlockPos.containing(player.position()), TETHER_BREAK.value(), SoundSource.PLAYERS, 0.5f, 1.0f);
            SoulTether.LOGGER.debug("Tether Item Broke !");
        } else {
            tetherStack.setDamageValue(newDamage);
        }

        // Drop existing items in the Soul Chest onto the ground at targetPos before transferring
        Containers.dropContents(targetLevel, targetPos, soulChest);
        soulChest.clearContent();

        // Move player's inventory items to the Soul Chest slot-to-slot
        boolean transferredAny = false;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            // Do not apply loss rate or destroy the tether itself (or its slot)
            if (i == tetherSlot) {
                if (tetherBroke) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
                continue;
            }

            // Check loss rate (items that roll below lossRate drop at death spot)
            if (tier.lossRate() > 0 && player.level().getRandom().nextDouble() < tier.lossRate()) {
                continue; // Skip transferring, it remains in player inventory and drops normally
            }

            // Copy to exact same slot in Soul Chest
            soulChest.setItem(i, stack.copy());
            player.getInventory().setItem(i, ItemStack.EMPTY);
            transferredAny = true;
        }

        // If the tether broke, we play a break sound and remove it
        if (tetherBroke) {
            player.getInventory().setItem(tetherSlot, ItemStack.EMPTY);
            player.playSound(
                    SoundEvents.ITEM_BREAK.value(),
                    1.0f,
                    1.0f);
        }

        if (transferredAny) {
            soulChest.setChanged();
            targetLevel.playSound(null, targetPos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5f, 1.0f);
        }
    }
}
