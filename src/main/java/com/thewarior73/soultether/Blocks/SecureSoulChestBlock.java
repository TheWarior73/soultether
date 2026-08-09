package com.thewarior73.soultether.Blocks;

import com.mojang.serialization.MapCodec;
import com.thewarior73.soultether.Items.SoulTetherItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SecureSoulChestBlock extends SoulChestBlock {
    public static final MapCodec<SecureSoulChestBlock> CODEC = simpleCodec(SecureSoulChestBlock::new);

    public SecureSoulChestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull MapCodec<? extends SoulChestBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new SecureSoulChestBlockEntity(pos, state);
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
        if (blockEntity instanceof SecureSoulChestBlockEntity secureChest) {
            if (secureChest.hasOwner() && !secureChest.isOwner(player)) {
                player.sendOverlayMessage(Component.translatable("block.soultether.secure_soul_chest.owner_locked", secureChest.getOwnerName()));
                return InteractionResult.CONSUME;
            }
            player.openMenu(secureChest);
        }

        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? createTickerHelper(blockEntityType, ModBlocks.SECURE_SOUL_CHEST_ENTITY_TYPE, SoulChestBlockEntity::lidAnimateTick) : null;
    }
}
