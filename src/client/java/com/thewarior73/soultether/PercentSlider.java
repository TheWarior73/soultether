package com.thewarior73.soultether;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;

public class PercentSlider extends AbstractSliderButton {
    private final Component prefix;
    private final DoubleConsumer onValueChange;

    public PercentSlider(int x, int y, int width, int height, Component prefix, double initialValue, DoubleConsumer onValueChange) {
        super(x, y, width, height, Component.empty(), initialValue);
        this.prefix = prefix;
        this.onValueChange = onValueChange;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        int percent = (int) Math.round(this.value * 100.0);
        this.setMessage(Component.empty().append(this.prefix).append(": ").append(Component.literal(percent + "%")));
    }

    @Override
    protected void applyValue() {
        this.onValueChange.accept(this.value);
    }
}
