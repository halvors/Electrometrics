package org.halvors.electrometrics.common.util.energy;

import org.halvors.electrometrics.common.ConfigurationManager.Client;
import org.halvors.electrometrics.common.ConfigurationManager.General;

public class EnergyUtils {
	/**
	 * Converts the energy to the default energy system.
	 * @param energy the raw energy.
	 * @return the energy as a String.
	 */
	public static String getEnergyDisplay(double energy) {
		EnergyUnit energyType = Client.energyUnitType;
		double multiplier = 1;

		switch (energyType) {
			case JOULES:
				multiplier = General.toJoules;
				break;

			case MINECRAFT_JOULES:
				multiplier = General.toMinecraftJoules;
				break;

			case ELECTRICAL_UNITS:
				multiplier = General.toElectricalUnits;
				break;
		}

		return EnergyDisplay.getDisplayShort(energy * multiplier, energyType);
	}
}
