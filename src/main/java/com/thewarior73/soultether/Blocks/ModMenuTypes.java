package com.thewarior73.soultether.Blocks;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final MenuType<SoulChestMenu> SOUL_CHEST_MENU_TYPE = Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath(SoulTether.MOD_ID, "soul_chest"),
            new MenuType<>(SoulChestMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void initialize() {
        SoulTether.LOGGER.debug("Initializing ModMenuTypes");
    }
}
