package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.Blocks.SoulChestBlock;
import com.thewarior73.soultether.SoulTether;
import com.thewarior73.soultether.config.ModConfig;
import net.minecraft.ChatFormatting;
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
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SoulTetherItem extends Item {
    private final ModConfig.TetherTier tier;

    public SoulTetherItem(Properties properties, ModConfig.TetherTier tier) {
        super(properties);
        this.tier = tier;
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

        SoulTether.LOGGER.debug("Block: {}", level.getBlockState(pos).getBlock());
        SoulTether.LOGGER.debug("Is instance of SoulChestBlock: {}", level.getBlockState(pos).getBlock() instanceof SoulChestBlock);

        if (level.getBlockState(pos).getBlock() instanceof SoulChestBlock) {
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }

            CustomData.update(DataComponents.CUSTOM_DATA, stack, nbt -> {
                nbt.putInt("x", pos.getX());
                nbt.putInt("y", pos.getY());
                nbt.putInt("z", pos.getZ());
                nbt.putString("dimension", level.dimension().location().toString());

                SoulTether.LOGGER.debug(nbt.toString());
            });

            if (player != null) {
                player.sendOverlayMessage(Component.translatable("item.soultether.soul_tether.linked", pos.getX(), pos.getY(), pos.getZ()));
                SoulTether.LOGGER.debug("linked: {} {} {}", pos.getX(), pos.getY(), pos.getZ());
                level.playSound(null, pos, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.8f, 1.2f);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
