package org.halvors.electrometrics.common.util.energy;

import org.halvors.electrometrics.Electrometrics;

public class EnergyUtils {
    /**
     * Converts the energy to the default energy system.
     * @param energy the raw energy.
     * @return the energy as a String.
     */
    public static String getEnergyDisplay(double energy) {
        Unit energyType = Electrometrics.energyUnitType;
        double multiplier = 0;

        switch (energyType) {
            case JOULES:
                multiplier = Electrometrics.toJoules;
                break;

            case MINECRAFT_JOULES:
                multiplier = Electrometrics.toMinecraftJoules;
                break;

            case ELECTRICAL_UNITS:
                multiplier = Electrometrics.toElectricalUnits;
                break;
        }

        return UnitDisplay.getDisplayShort(energy * multiplier, energyType);
    }
}
