package com.thewarior73.soultether.Items;

import com.thewarior73.soultether.SoulTether;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIDs {
    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, SoulTether.id(name));
    }

    public static final ResourceKey<Item> SOUL_TETHER = create("soul_tether");
    public static final ResourceKey<Item> SOUL_TETHER_IRON = create("soul_tether_iron");
    public static final ResourceKey<Item> SOUL_TETHER_GOLD = create("soul_tether_gold");
    public static final ResourceKey<Item> SOUL_TETHER_DIAMOND = create("soul_tether_diamond");
    public static final ResourceKey<Item> SOUL_CHEST = create("soul_chest");
    public static final ResourceKey<Item> SECURE_SOUL_CHEST = create("secure_soul_chest");
}
