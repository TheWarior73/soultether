package com.thewarior73.soultether;

import com.thewarior73.soultether.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public ConfigScreen(Screen parent) {
        super(Component.translatable("gui.soultether.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(this.title, this.font);

        LinearLayout content = this.layout.addToContents(LinearLayout.vertical().spacing(8));

        content.addChild(CycleButton.onOffBuilder(ModConfig.INSTANCE.enableItemLoss)
                .create(0, 0, 240, 20,
                        Component.translatable("gui.soultether.config.enable_item_loss"),
                        (button, value) -> ModConfig.INSTANCE.enableItemLoss = value));

        content.addChild(CycleButton.onOffBuilder(ModConfig.INSTANCE.enableDebugLogs)
                .create(0, 0, 240, 20,
                        Component.translatable("gui.soultether.config.enable_debug_logs"),
                        (button, value) -> ModConfig.INSTANCE.enableDebugLogs = value));

        content.addChild(Button.builder(Component.translatable("gui.soultether.config.item_loss_rates"),
                button -> {
                    Minecraft.getInstance().gui.setScreen(new ItemLossConfigScreen(this));
                })
                .width(240)
                .build());

        content.addChild(Button.builder(Component.translatable("gui.soultether.config.dimentional_cost_mult"),
                        button -> {
                            Minecraft.getInstance().gui.setScreen(new DimentionalCostMultConfigScreen(this));
                        })
                .width(240)
                .build());

        content.addChild(Button.builder(Component.translatable("gui.soultether.config.reset").withStyle(ChatFormatting.RED),
                button -> {
                    ModConfig.resetModConfig();
                    this.onClose();
                })
                .width(240)
                .build());


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