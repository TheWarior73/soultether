package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIDs {
    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SoulTether.MOD_ID, name));
    }

    public static final ResourceKey<Item> SOUL_TETHER = create("soul_tether");
    public static final ResourceKey<Item> THUNDER_STICK = create("thunder_stick");
}
