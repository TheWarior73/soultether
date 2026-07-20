package com.thewarior73.soultether.Items;

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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

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
        net.minecraft.core.BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockState(pos).getBlock() instanceof SoulChestBlock) {
            if (level.isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            boolean isLinkedToThisChest = false;
            if (customData != null) {
                CompoundTag nbt = customData.copyTag();

                Optional<TetherLocation> optLocation = TetherLocation.readNbtData(nbt);

                if (TetherLocation.hasLocationData(nbt) &&  optLocation.isPresent()) {
                    BlockPos targetPos = optLocation.get().pos();
                    String dimString = optLocation.get().dimension();

                    if (targetPos.equals(pos) && dimString.equals(level.dimension().identifier().toString())) {
                        isLinkedToThisChest = true;
                    }
                }
            }

            if (isLinkedToThisChest) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                    nbt.remove("x");
                    nbt.remove("y");
                    nbt.remove("z");
                    nbt.remove("dimension");
                });

                if (player != null) {
                    player.sendOverlayMessage(Component.translatable("item.soultether.soul_tether.tooltip.unlinked_pos", pos.getX(), pos.getY(), pos.getZ()));
                    level.playSound(null, pos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.8f, 1.2f);
                    SoulTether.LOGGER.debug("Unlinked: {} {} {}", pos.getX(), pos.getY(), pos.getZ());
                }
            } else {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                    nbt.putInt("x", pos.getX());
                    nbt.putInt("y", pos.getY());
                    nbt.putInt("z", pos.getZ());
                    nbt.putString("dimension", level.dimension().identifier().toString());

                    SoulTether.LOGGER.debug(nbt.toString());
                });

                if (player != null) {
                    player.sendOverlayMessage(Component.translatable("item.soultether.soul_tether.tooltip.linked_pos", pos.getX(), pos.getY(), pos.getZ()));
                    level.playSound(null, pos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.8f, 1.2f);
                    SoulTether.LOGGER.debug("linked: {} {} {}", pos.getX(), pos.getY(), pos.getZ());
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
