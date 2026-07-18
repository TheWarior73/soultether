package com.thewarior73.soultether.client;

import com.thewarior73.soultether.Blocks.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class SoulTetherClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.SOUL_CHEST_MENU_TYPE, SoulChestScreen::new);
	}
}