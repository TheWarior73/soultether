package com.thewarior73.soultether.client;

import com.thewarior73.soultether.Blocks.ModBlocks;
import com.thewarior73.soultether.Blocks.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class SoulTetherClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.SOUL_CHEST_MENU_TYPE, SoulChestScreen::new);
		BlockEntityRenderers.register(ModBlocks.SOUL_CHEST_ENTITY_TYPE, SoulChestRenderer::new);
	}
}