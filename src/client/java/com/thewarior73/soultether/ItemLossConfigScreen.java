package com.thewarior73.soultether;

import com.thewarior73.soultether.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ItemLossConfigScreen extends Screen {
    private final Screen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public ItemLossConfigScreen(Screen parent) {
        super(Component.translatable("gui.soultether.config.item_loss_rates.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);

        LinearLayout content = this.layout.addToContents(LinearLayout.vertical().spacing(8));

        content.addChild(new StringWidget(Component.translatable("gui.soultether.config.item_loss_rates.description"), this.font));

        content.addChild(new PercentSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether"),
                ModConfig.INSTANCE.basicLossRate,
                val -> ModConfig.INSTANCE.basicLossRate = val));

        content.addChild(new PercentSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether_iron"),
                ModConfig.INSTANCE.ironLossRate,
                val -> ModConfig.INSTANCE.ironLossRate = val));

        content.addChild(new PercentSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether_gold"),
                ModConfig.INSTANCE.goldLossRate,
                val -> ModConfig.INSTANCE.goldLossRate = val));

        content.addChild(new PercentSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether_diamond"),
                ModConfig.INSTANCE.diamondLossRate,
                val -> ModConfig.INSTANCE.diamondLossRate = val));

        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        ModConfig.save();
        Minecraft.getInstance().gui.setScreen(this.parent);
    }
}
