package com.thewarior73.soultether.Blocks;

import com.thewarior73.soultether.Items.SoulTetherItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import com.mojang.serialization.MapCodec;

public class SoulChestBlock extends BaseEntityBlock {
    public static final MapCodec<SoulChestBlock> CODEC = simpleCodec(SoulChestBlock::new);

    public SoulChestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulChestBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, Player player, @NonNull BlockHitResult hitResult) {
        if (player.getMainHandItem().getItem() instanceof SoulTetherItem || player.getOffhandItem().getItem() instanceof SoulTetherItem) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SoulChestBlockEntity soulChest) {
            boolean transferredAny = false;
            for (int i = 0; i < soulChest.getContainerSize(); i++) {
                ItemStack stack = soulChest.getItem(i);
                if (!stack.isEmpty()) {
                    // Try to insert into player's inventory
                    if (player.getInventory().add(stack)) {
                        soulChest.setItem(i, ItemStack.EMPTY);
                        transferredAny = true;
                    } else {
                        // Inventory is full, drop on the ground
                        player.drop(stack, false);
                        soulChest.setItem(i, ItemStack.EMPTY);
                        transferredAny = true;
                    }
                }
            }

            if (transferredAny) {
                soulChest.setChanged();
                level.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5f, level.getRandom().nextFloat() * 0.1f + 0.9f);
            }
        }

        return InteractionResult.CONSUME;
    }
}
