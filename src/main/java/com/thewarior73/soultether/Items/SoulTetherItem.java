package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.Blocks.SecureSoulChestBlockEntity;
import com.thewarior73.soultether.Blocks.SoulChestBlock;
import com.thewarior73.soultether.SoulTether;
import com.thewarior73.soultether.TetherLocation;
import com.thewarior73.soultether.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Optional;

public class SoulTetherItem extends Item {
    private final ModConfig.TetherTier tier;

    public SoulTetherItem(Properties properties, ModConfig.TetherTier tier) {
        super(properties);
        this.tier = tier;
    }

    public ModConfig.TetherTier getTier() {
        return this.tier;
    }

    public static boolean isLinked(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag nbt = customData.copyTag();

            return TetherLocation.hasLocationData(nbt);
        }
        return false;
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockState(pos).getBlock() instanceof SoulChestBlock) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            SoulTether.LOGGER.debug(
                    "++++ SoulTether Used ++++\n" +
                    "[***                 ***]"
            );

            BlockEntity blockEntity = level.getBlockEntity(pos);
            SecureSoulChestBlockEntity secureChest = (blockEntity instanceof SecureSoulChestBlockEntity s) ? s : null;

            if (secureChest != null && player != null) {
                SoulTether.LOGGER.debug("+++ Secure Chest Detected");
                SoulTether.LOGGER.debug("Is Player Owner ? {}", secureChest.isOwner(player));

                // Check if someone else owns this secure chest
                if (secureChest.hasOwner() && !secureChest.isOwner(player)) {
                    player.sendOverlayMessage(Component.translatable("block.soultether.secure_soul_chest.owner_locked", secureChest.getOwnerName()));

                    SoulTether.LOGGER.debug("Soul Chest belongs to someone else ! {}", secureChest.getOwnerName());

                    return InteractionResult.SUCCESS;
                }
            }

            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            boolean isLinkedToThisChest = false;
            if (customData != null) {
                CompoundTag nbt = customData.copyTag();

                Optional<TetherLocation> optLocation = TetherLocation.readNbtData(nbt);

                if (TetherLocation.hasLocationData(nbt) && optLocation.isPresent()) {
                    BlockPos targetPos = optLocation.get().pos();
                    String dimString = optLocation.get().dimension();

                    if (targetPos.equals(pos) && dimString.equals(level.dimension().identifier().toString())) {
                        isLinkedToThisChest = true;
                    }
                }
            }

            // Removing link
            if (isLinkedToThisChest) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                    nbt.remove("x");
                    nbt.remove("y");
                    nbt.remove("z");
                    nbt.remove("dimension");
                });

                if (secureChest != null) {
                    secureChest.setLinked(false);
                }

                if (player != null) {
                    player.sendOverlayMessage(Component.translatable("item.soultether.soul_tether.tooltip.unlinked_pos", pos.getX(), pos.getY(), pos.getZ()));
                    level.playSound(null, pos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.8f, 1.2f);

                    // Removing enchantment glint
                    stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

                    // Removing the Linked Lore Component
                    stack.set(DataComponents.LORE, ItemLore.EMPTY);

                    SoulTether.LOGGER.debug("Unlinked: {} {} {}", pos.getX(), pos.getY(), pos.getZ());
                }

            // Adding the link
            } else {
                SoulTether.LOGGER.debug("is secure chest Null: {}", secureChest == null);

                if (secureChest != null && secureChest.isLinked()) {
                    SoulTether.LOGGER.debug("is secure chest Linked: {}", secureChest.isLinked());
                    if (player != null) {
                        player.sendOverlayMessage(Component.translatable("block.soultether.secure_soul_chest.already_linked"));
                    }
                    return InteractionResult.SUCCESS;
                }

                CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                    nbt.putInt("x", pos.getX());
                    nbt.putInt("y", pos.getY());
                    nbt.putInt("z", pos.getZ());
                    nbt.putString("dimension", level.dimension().identifier().toString());

                    SoulTether.LOGGER.debug(nbt.toString());
                });

                if (secureChest != null && player != null) {
                    if (!secureChest.hasOwner()) {
                        secureChest.setOwner(player);
                    } else {
                        secureChest.setLinked(true);
                    }
                }

                if (player != null) {
                    player.sendOverlayMessage(Component.translatable("item.soultether.soul_tether.tooltip.linked_pos", pos.getX(), pos.getY(), pos.getZ()));
                    level.playSound(null, pos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.8f, 1.2f);

                    // Applying enchantment glint
                    stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

                    // Adding the Linked Lore Component
                    ItemLore LinkedTetherLore = getItemLore(pos, player);
                    stack.set(DataComponents.LORE, LinkedTetherLore);

                    SoulTether.LOGGER.debug("linked: {} {} {}", pos.getX(), pos.getY(), pos.getZ());
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    private static @NonNull ItemLore getItemLore(BlockPos pos, Player player) {
        // Assumes the player is in the same dimension as the right-clicked chest. Uses the local player dimension as a result
        String localDimString = player.level().dimension().identifier().toString();

        Component LinkedChestComponent = Component.translatable("item.soultether.soul_tether.tooltip.linked_pos", pos.getX(), pos.getY(), pos.getZ());
        Component LinkedDimComponent = Component.translatable("item.soultether.soul_tether.tooltip.linked_dim", localDimString);

        ArrayList<Component> ComponentList = new ArrayList<>();
        ComponentList.add(LinkedChestComponent);
        ComponentList.add(LinkedDimComponent);

        return new ItemLore(ComponentList);
    }
}
