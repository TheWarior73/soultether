package com.thewarior73.soultether.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thewarior73.soultether.SoulTether;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("soultether.json");

    public static ModConfig INSTANCE = new ModConfig();

//  #region Default Config
    public boolean enableItemLoss = true;
    public boolean enableDebugLogs = false;

    public double basicLossRate = 0.15;
    public double ironLossRate = 0.10;
    public double goldLossRate = 0.01;
    public double diamondLossRate = 0.0;

    public double basicDimCostMult = 1.0;
    public double ironDimCostMult = 2.0;
    public double goldDimCostMult = 3.0;
    public double diamondDimCostMult = 5.0;
//  #endregion

    public record TetherTier(int maxUses, double defaultLossRate, double defaultDimensionalCostMultiplier) {
        public double lossRate() {
            return ModConfig.getEffectiveLossRate(this);
        }
        public double dimensionalCostMultiplier() { return ModConfig.getEffectiveDimensionalCostMultiplier(this); }
    }

    public static final TetherTier BASIC = new TetherTier(1, 0.15, 1.0);
    public static final TetherTier IRON = new TetherTier(10, 0.10, 2.0);
    public static final TetherTier GOLD = new TetherTier(5, 0.01, 3.0);
    public static final TetherTier DIAMOND = new TetherTier(25, 0.0, 5.0);

    public static double getEffectiveLossRate(TetherTier tier) {
        if (INSTANCE == null || !INSTANCE.enableItemLoss) {
            return 0.0;
        }
        if (tier == BASIC) return INSTANCE.basicLossRate;
        if (tier == IRON) return INSTANCE.ironLossRate;
        if (tier == GOLD) return INSTANCE.goldLossRate;
        if (tier == DIAMOND) return INSTANCE.diamondLossRate;
        return tier.defaultLossRate();
    }

    public static double getEffectiveDimensionalCostMultiplier(TetherTier tier) {
        if (INSTANCE == null) {
            return tier.defaultDimensionalCostMultiplier();
        }
        if (tier == BASIC) return INSTANCE.basicDimCostMult;
        if (tier == IRON) return INSTANCE.ironDimCostMult;
        if (tier == GOLD) return INSTANCE.goldDimCostMult;
        if (tier == DIAMOND) return INSTANCE.diamondDimCostMult;
        return tier.defaultDimensionalCostMultiplier();
    }

    public static void resetModConfig() {
        INSTANCE.enableItemLoss = true;
        INSTANCE.enableDebugLogs = false;

        INSTANCE.basicLossRate = 0.15;
        INSTANCE.ironLossRate = 0.10;
        INSTANCE.goldLossRate = 0.01;
        INSTANCE.diamondLossRate = 0.0;

        INSTANCE.basicDimCostMult = 1.0;
        INSTANCE.ironDimCostMult = 2.0;
        INSTANCE.goldDimCostMult = 3.0;
        INSTANCE.diamondDimCostMult = 5.0;

        save();
    }

    public static void load() {
        File file = CONFIG_PATH.toFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                ModConfig config = GSON.fromJson(reader, ModConfig.class);
                if (config != null) {
                    INSTANCE = config;
                    SoulTether.LOGGER.info("Loaded SoulTether config");
                    return;
                }
            } catch (Exception e) {
                SoulTether.LOGGER.error("Failed to load SoulTether config, using default", e);
            }
        }
        INSTANCE = new ModConfig();
        save();
    }

    public static void save() {
        try {
            File file = CONFIG_PATH.toFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            SoulTether.LOGGER.error("Failed to save SoulTether config", e);
        }
    }
}
