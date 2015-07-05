package org.halvors.electrometrics.common.block;

public enum ElectricityMeterType {
    BASIC(2000000, 800),
    ADVANCED(8000000, 3200),
    ELITE(32000000, 12800),
    ULTIMATE(128000000, 51200),
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    public double maxEnergy;
    private double baseMaxEnergy;

    public double output;
    private double baseOutput;

    ElectricityMeterType(double max, double out) {
        baseMaxEnergy = maxEnergy = max;
        baseOutput = output = out;
    }

    public static ElectricityMeterType getFromName(String tierName) {
        for (ElectricityMeterType tier : values()) {
            if (tierName.contains(tier.getTier().getName())) {
                return tier;
            }
        }

        return BASIC;
    }

    public Tier getTier() {
        return Tier.values()[ordinal()];
    }
}
