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

public class DimentionalCostMultConfigScreen extends Screen {
    private final Screen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public DimentionalCostMultConfigScreen(Screen parent) {
        super(Component.translatable("gui.soultether.config.dimentional_cost_mult.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);

        LinearLayout content = this.layout.addToContents(LinearLayout.vertical().spacing(8));

        content.addChild(new StringWidget(Component.translatable("gui.soultether.config.dimentional_cost_mult.description"), this.font));

        content.addChild(new IntSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether"),
                1, ModConfig.BASIC.maxUses(), (int) ModConfig.INSTANCE.basicDimCostMult,
                val -> ModConfig.INSTANCE.basicDimCostMult = val));

        content.addChild(new IntSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether_iron"),
                1, ModConfig.IRON.maxUses(), (int) ModConfig.INSTANCE.ironDimCostMult,
                val -> ModConfig.INSTANCE.ironDimCostMult = val));

        content.addChild(new IntSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether_gold"),
                1, ModConfig.GOLD.maxUses(), (int) ModConfig.INSTANCE.goldDimCostMult,
                val -> ModConfig.INSTANCE.goldDimCostMult = val));

        content.addChild(new IntSlider(0, 0, 240, 20,
                Component.translatable("item.soultether.soul_tether_diamond"),
                1, ModConfig.DIAMOND.maxUses(), (int) ModConfig.INSTANCE.diamondDimCostMult,
                val -> ModConfig.INSTANCE.diamondDimCostMult = val));

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
        if (this.minecraft != null) {
            Minecraft.getInstance().gui.setScreen(this.parent);
        }
    }
}
