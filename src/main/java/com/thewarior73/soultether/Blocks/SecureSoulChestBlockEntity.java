package com.thewarior73.soultether.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class SecureSoulChestBlockEntity extends SoulChestBlockEntity {
    private UUID ownerUUID;
    private String ownerName;
    private boolean isLinked;

    public SecureSoulChestBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public String getOwnerName() {
        return this.ownerName != null ? this.ownerName : "Unknown";
    }

    public boolean hasOwner() {
        return this.ownerUUID != null;
    }

    public boolean isOwner(Player player) {
        return player != null && getOwnerUUID() != null && getOwnerUUID().equals(player.getUUID());
    }

    public void setOwner(Player player) {
        if (player != null) {
            this.ownerUUID = player.getUUID();
            this.ownerName = player.getScoreboardName();
            this.setChanged();
        }
    }

    public boolean isLinked() {
        return this.isLinked;
    }

    public void setLinked(boolean linked) {
        this.isLinked = linked;
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        if (!super.stillValid(player)) {
            return false;
        }
        return !hasOwner() || isOwner(player);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("container.soultether.secure_soul_chest");
    }

    @Override
    protected void loadAdditional(final @NonNull ValueInput input) {
        super.loadAdditional(input);
        input.getString("OwnerUUID").ifPresent(uuidStr -> {
            try {
                this.ownerUUID = UUID.fromString(uuidStr);
            } catch (Exception ignored) {
                this.ownerUUID = null;
            }
        });
        input.getString("OwnerName").ifPresent(name -> this.ownerName = name);
        this.isLinked = input.getBooleanOr("IsLinked", false); // TODO test
    }

    @Override
    protected void saveAdditional(final @NonNull ValueOutput output) {
        super.saveAdditional(output);
        if (this.ownerUUID != null) {
            output.putString("OwnerUUID", this.ownerUUID.toString());
        }
        if (this.ownerName != null) {
            output.putString("OwnerName", this.ownerName);
        }
        output.putBoolean("IsLinked", this.isLinked);
    }
}
