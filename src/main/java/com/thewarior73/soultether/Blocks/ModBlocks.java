package com.thewarior73.soultether.Blocks;

import com.thewarior73.soultether.SoulTether;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static void initialize() {
        // Loads the class at runtime
    }

    // region register function definition
    private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        // Create the block instance
        Block block = blockFactory.apply(properties.setId(id));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(SoulTether.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
    // endregion

    public static final Block SOUL_CHEST = register(
            ModBlockIDs.SOUL_CHEST,
            Block::new,
            BlockBehaviour.Properties.of()
                    .setId(ModBlockIDs.SOUL_CHEST)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
    );

    public static final BlockEntityType<SoulChestBlockEntity> SOUL_CHEST_ENTITY_TYPE = register(
            "soul_chest_entity", SoulChestBlockEntity::new, ModBlocks.SOUL_CHEST
    );
}
