package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.SoulTether;
import com.thewarior73.soultether.config.ModConfig;
import com.thewarior73.soultether.Blocks.ModBlocks;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

import static com.thewarior73.soultether.SoulTether.MOD_ID;

public class ModItems {

    // Loads the class at runtime
    public static void initialize() {
        // region register_creative_tab
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
        // endregion register_creative_tab

        SoulTether.LOGGER.debug("ModItems Initialized");
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        SoulTether.LOGGER.debug("Registered Item: {}", itemKey);

        return item;
    }

    public static final Item SOUL_TETHER = register(
            ModItemIDs.SOUL_TETHER,
            p -> new SoulTetherItem(p, ModConfig.BASIC),
            new Item.Properties().durability(ModConfig.BASIC.maxUses())
    );
    public static final Item SOUL_TETHER_IRON = register(
            ModItemIDs.SOUL_TETHER_IRON,
            p -> new SoulTetherItem(p, ModConfig.IRON),
            new Item.Properties().durability(ModConfig.IRON.maxUses())
    );
    public static final Item SOUL_TETHER_GOLD = register(
            ModItemIDs.SOUL_TETHER_GOLD,
            p -> new SoulTetherItem(p, ModConfig.GOLD),
            new Item.Properties().durability(ModConfig.GOLD.maxUses())
    );
    public static final Item SOUL_TETHER_DIAMOND = register(
            ModItemIDs.SOUL_TETHER_DIAMOND,
            p -> new SoulTetherItem(p, ModConfig.DIAMOND),
            new Item.Properties().durability(ModConfig.DIAMOND.maxUses())
    );
    public static final Item SOUL_CHEST = register(
            ModItemIDs.SOUL_CHEST,
            p -> new BlockItem(ModBlocks.SOUL_CHEST, p),
            new Item.Properties()
    );
    public static final Item SECURE_SOUL_CHEST = register(
            ModItemIDs.SECURE_SOUL_CHEST,
            p -> new BlockItem(ModBlocks.SECURE_SOUL_CHEST, p),
            new Item.Properties()
    );

    // region Creative Tab Item register

    public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(MOD_ID, "creative_tab")
    );

    public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.SOUL_TETHER))
            .title(Component.translatable("creativeTab.soultether"))
            .displayItems((params, output) -> {
                // Items
                output.accept(ModItems.SOUL_TETHER);
                output.accept(ModItems.SOUL_TETHER_IRON);
                output.accept(ModItems.SOUL_TETHER_GOLD);
                output.accept(ModItems.SOUL_TETHER_DIAMOND);

                // Blocks
                output.accept(ModItems.SOUL_CHEST);
                output.accept(ModItems.SECURE_SOUL_CHEST);
            })
            .build();

    // endregion Creative Tab Item register
}
