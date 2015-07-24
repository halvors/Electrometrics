package org.halvors.electrometrics.common.base;

import org.halvors.electrometrics.common.util.LanguageUtils;
import org.halvors.electrometrics.common.util.render.Color;

public class Tier {
	public enum Base {
		BASIC("Basic", Color.BRIGHT_GREEN),
		ADVANCED("Advanced", Color.DARK_RED),
		ELITE("Elite", Color.DARK_BLUE),
		ULTIMATE("Ultimate", Color.PURPLE);

		private final String name;
		private final Color color;

		Base(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String getUnlocalizedName() {
			return name;
		}

		public String getLocalizedName() {
			return LanguageUtils.translate("tier." + name);
		}

		public Color getColor() {
			return color;
		}
	}

	public enum ElectricityMeter {
		BASIC(5000000, 2000), // 800 J
		ADVANCED(20000000, 8000), // 3200 J
		ELITE(80000000, 32000), // 12800 J
		ULTIMATE(320000000, 128000); // 51200 J

		private final int maxEnergy;
		private final int maxTransfer;

		ElectricityMeter(int maxEnergy, int maxTransfer) {
			this.maxEnergy = maxEnergy;
			this.maxTransfer = maxTransfer;
		}

		public Base getBase() {
			return Base.values()[ordinal()];
		}

		public int getMaxEnergy() {
			return maxEnergy;
		}

		public int getMaxTransfer() {
			return maxTransfer;
		}

		public MachineType getMachineType() {
			return MachineType.values()[ordinal()];
		}

		public static ElectricityMeter getFromMachineType(MachineType machineType) {
			return values()[machineType.getMetadata()];
		}
	}
}