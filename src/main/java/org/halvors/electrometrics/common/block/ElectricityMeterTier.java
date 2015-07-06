package org.halvors.electrometrics.common.block;

public enum ElectricityMeterTier {
    BASIC(0, 2000), // 800
    ADVANCED(0, 8000), // 8000
    ELITE(0, 32000), // 12800
    ULTIMATE(0, 128000), //51200
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public double maxEnergy;
    private double baseMaxEnergy;

    public double output;
    private double baseOutput;

    ElectricityMeterTier(double max, double out) {
        baseMaxEnergy = maxEnergy = max;
        baseOutput = output = out;
    }

    public static ElectricityMeterTier getFromName(String tierName) {
        for (ElectricityMeterTier tier : values()) {
            if (tierName.contains(tier.getBaseTier().getName())) {
                return tier;
            }
        }

        return BASIC;
    }

    public Tier getBaseTier() {
        return Tier.values()[ordinal()];
    }
}
