package com.thewarior73.soultether.client;

import com.thewarior73.soultether.Blocks.SoulChestMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class SoulChestScreen extends AbstractContainerScreen<SoulChestMenu> {
    private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

    public SoulChestScreen(SoulChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 222);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        this.extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256
        );
    }
}
