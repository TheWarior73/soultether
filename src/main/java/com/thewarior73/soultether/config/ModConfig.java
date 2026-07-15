package com.thewarior73.soultether.config;

public class ModConfig {
    /**
     * @param lossRate                  e.g. 0.2 means 20% items lost on death
     * @param dimensionalCostMultiplier e.g. 3.0 means 3x durability cost if dimensions differ
     */
    public record TetherTier(int maxUses, double lossRate, double dimensionalCostMultiplier) {
    }

    public static final TetherTier BASIC = new TetherTier(1, 0.15, 3.0);
    public static final TetherTier IRON = new TetherTier(10, 0.10, 2.0);
    public static final TetherTier GOLD = new TetherTier(5, 0.0, 1.5);
    public static final TetherTier DIAMOND = new TetherTier(25, 0.0, 1.0);
}
