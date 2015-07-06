package org.halvors.electrometrics.common.base;

public enum ElectricityMeterTier {
    BASIC(0, 2000), // 800
    ADVANCED(0, 8000), // 8000
    ELITE(0, 32000), // 12800
    ULTIMATE(0, 128000), // 51200
    CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private int maxEnergy;
    private int maxTransfer;

    ElectricityMeterTier(int maxEnergy, int maxTransfer) {
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
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

    public int getMaxTransfer() {
        return maxTransfer;
    }
}
