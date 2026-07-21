package com.thewarior73.soultether.Blocks;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SoulChestBlockEntity extends BlockEntity implements Container, MenuProvider {
    private final NonNullList<ItemStack> items = NonNullList.withSize(54, ItemStack.EMPTY);

    public SoulChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SOUL_CHEST_ENTITY_TYPE, pos, state);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NonNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public @NonNull ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    @Override
    public @NonNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, @NonNull ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    @Override
    protected void loadAdditional(final @NonNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    protected void saveAdditional(final @NonNull ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("container.soultether.soul_chest");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInventory, @NonNull Player player) {
        return new SoulChestMenu(containerId, playerInventory, this);
    }

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        {
            SoulTether.LOGGER.debug("[SoulChestBlockEntity] openersCounter created");
        }

        @Override
        protected void onOpen(final @NonNull Level level, final @NonNull BlockPos pos, final BlockState blockState) {
            SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] onOpen called");
            if (blockState.getBlock() instanceof SoulChestBlock chestBlock) {
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] onOpen sound played");
            }
        }

        @Override
        protected void onClose(final @NonNull Level level, final @NonNull BlockPos pos, final BlockState blockState) {
            SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] onClose called");
            if (blockState.getBlock() instanceof SoulChestBlock chestBlock) {
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] onClose sound played");
            }
        }

        @Override
        protected void openerCountChanged(final @NonNull Level level, final @NonNull BlockPos pos, final @NonNull BlockState blockState, final int previous, final int current) {
            SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] OpenerCountChanged called");
            SoulChestBlockEntity.this.signalOpenCount(level, pos, blockState, previous, current);
        }

        @Override
        public boolean isOwnContainer(final Player player) {
            SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] isOwnContainer called");
            if (!(player.containerMenu instanceof ChestMenu)) {
                return false;
            } else {
                Container container = ((ChestMenu)player.containerMenu).getContainer();
                SoulTether.LOGGER.debug("[SoulChestBlockEntity.openerCounter] result: {}", container == SoulChestBlockEntity.this
                        || container instanceof CompoundContainer compoundContainer && compoundContainer.contains(SoulChestBlockEntity.this));
                return container == SoulChestBlockEntity.this
                        || container instanceof CompoundContainer compoundContainer && compoundContainer.contains(SoulChestBlockEntity.this);
            }
        }
    };

    @Override
    public void startOpen(final @NonNull ContainerUser containerUser) {
        if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
            assert this.getLevel() != null;
            this.openersCounter
                    .incrementOpeners(
                            containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState(), containerUser.getContainerInteractionRange()
                    );
        }
    }

    @Override
    public void stopOpen(final @NonNull ContainerUser containerUser) {
        if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
            assert this.getLevel() != null;
            this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void signalOpenCount(final Level level, final BlockPos pos, final BlockState blockState, final int previous, final int current) {
        Block block = blockState.getBlock();
        level.blockEvent(pos, block, 1, current);
    }

}
