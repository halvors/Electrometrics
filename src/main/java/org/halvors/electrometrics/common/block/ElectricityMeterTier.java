package org.halvors.electrometrics.common.block;

public enum ElectricityMeterTier {
    BASIC(0, 2000, 2000), // 800
    ADVANCED(0, 8000, 8000), // 8000
    ELITE(0, 32000, 32000), // 12800
    ULTIMATE(0, 128000, 128000), // 51200
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private int maxEnergy;
    private int maxReceive;
    private int maxExtract;

    ElectricityMeterTier(int maxEnergy, int maxReceive, int maxExtract) {
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
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

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }
}
