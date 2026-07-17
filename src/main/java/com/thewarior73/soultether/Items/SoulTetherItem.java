package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.Blocks.SoulChestBlock;
import com.thewarior73.soultether.SoulTether;
import com.thewarior73.soultether.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;
import java.util.List;

public class SoulTetherItem extends Item {
    private final ModConfig.TetherTier tier;
    private Block LinkedChest;

    public SoulTetherItem(Properties properties, ModConfig.TetherTier tier) {
        super(properties);
        this.tier = tier;
        this.LinkedChest = null;
    }

    public ModConfig.TetherTier getTier() {
        return this.tier;
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

            SoulTether.LOGGER.debug("Block: {}", level.getBlockState(pos).getBlock());
            SoulTether.LOGGER.debug("Is instance of SoulChestBlock: {}", level.getBlockState(pos).getBlock() instanceof SoulChestBlock);
            SoulTether.LOGGER.debug("IsSameSoulChest: {}", isSameSoulChest(LinkedChest, level.getBlockState(pos).getBlock()));

            if (isSameSoulChest(LinkedChest, level.getBlockState(pos).getBlock())) {
                CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                    nbt.remove("x");
                    nbt.remove("y");
                    nbt.remove("z");
                    nbt.remove("dimension");
                });

                this.LinkedChest = null;

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

                    this.LinkedChest = level.getBlockState(pos).getBlock();

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

    private boolean isSameSoulChest(Block b1, Block b2) {
        boolean result = false;
        if (b1 != null && b2 != null) {
            if (b1 instanceof SoulChestBlock && b2 instanceof SoulChestBlock) {
                result = b1.equals(b2);
            }
        }
        return result;
    }
}
