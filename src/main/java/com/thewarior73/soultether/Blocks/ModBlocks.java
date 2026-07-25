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
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class ModBlocks {
    public static void initialize() {
        // Loads the class at runtime
        SoulTether.LOGGER.debug("ModBlocks Initialized");
    }

    // region register function definition
    private static Block register(ResourceKey<Block> id, @NonNull Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.@NonNull Properties properties) {
        // Create the block instance
        Block block = blockFactory.apply(properties.setId(id));

        SoulTether.LOGGER.debug("Registering ModBlock: {}", id);

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = SoulTether.id(name);

        SoulTether.LOGGER.debug("Registering BlockEntityType: {}", id);

        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
    // endregion

    // region soul chest
    public static final Block SOUL_CHEST = register(
            ModBlockIDs.SOUL_CHEST,
            SoulChestBlock::new,
            BlockBehaviour.Properties.of()
                    .setId(ModBlockIDs.SOUL_CHEST)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
    );

    public static final BlockEntityType<SoulChestBlockEntity> SOUL_CHEST_ENTITY_TYPE = register(
            "soul_chest_entity", SoulChestBlockEntity::new, ModBlocks.SOUL_CHEST
    );
    // endregion
}
