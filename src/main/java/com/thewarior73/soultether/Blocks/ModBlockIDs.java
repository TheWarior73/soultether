package com.thewarior73.soultether.Blocks;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockIDs {

    public static ResourceKey<Block> createBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, SoulTether.id(name));
    }

    public static ResourceKey<BlockEntityType<?>> createBlockEntity(String name) {
        return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, SoulTether.id(name));
    }

    public static final ResourceKey<Block> SOUL_CHEST = createBlock("soul_chest");
    public static final ResourceKey<BlockEntityType<?>> SOUL_CHEST_ENTITY = createBlockEntity("soul_chest");
}
