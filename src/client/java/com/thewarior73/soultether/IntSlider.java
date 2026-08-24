package com.thewarior73.soultether;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;

public class IntSlider extends AbstractSliderButton {
    private final Component prefix;
    private final int minValue;
    private final int maxValue;
    private final IntConsumer onValueChange;

    public IntSlider(int x, int y, int width, int height, Component prefix, int minValue, int maxValue, int initialValue, IntConsumer onValueChange) {
        super(x, y, width, height, Component.empty(),
                maxValue > minValue ? (double) (Math.max(minValue, Math.min(maxValue, initialValue)) - minValue) / (double) (maxValue - minValue) : 0.0);
        this.prefix = prefix;
        this.minValue = minValue;
        this.maxValue = Math.max(minValue, maxValue);
        this.onValueChange = onValueChange;
        this.active = (maxValue > minValue);
        this.updateMessage();
    }

    private int getIntValue() {
        if (this.maxValue <= this.minValue) {
            return this.minValue;
        }
        return this.minValue + (int) Math.round(this.value * (this.maxValue - this.minValue));
    }

    @Override
    protected void updateMessage() {
        int current = getIntValue();
        this.setMessage(Component.empty().append(this.prefix).append(": ").append(Component.literal(current + "x")));
    }

    @Override
    protected void applyValue() {
        this.onValueChange.accept(getIntValue());
    }
}
