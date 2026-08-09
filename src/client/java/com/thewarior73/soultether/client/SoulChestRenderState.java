package com.thewarior73.soultether.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class SoulChestRenderState extends BlockEntityRenderState {
    public Direction facing = Direction.NORTH;
    public float open;
    public boolean isSecure;
}
