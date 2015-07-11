package org.halvors.electrometrics.common.base;

import org.halvors.electrometrics.common.util.Color;
import org.halvors.electrometrics.common.util.Utils;

public class Tier {
	public enum BaseTier {
		BASIC("Basic", Color.BRIGHT_GREEN),
		ADVANCED("Advanced", Color.DARK_RED),
		ELITE("Elite", Color.DARK_BLUE),
		ULTIMATE("Ultimate", Color.PURPLE),
		CREATIVE("Creative", Color.BLACK);

		private final String name;
		private final Color color;

		BaseTier(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String getUnlocalizedName() {
			return name;
		}

		public String getLocalizedName() {
			return Utils.translate("tier." + name);
		}

		public Color getColor() {
			return color;
		}

		public boolean isObtainable() {
			return this != CREATIVE;
		}
	}

	public enum ElectricityMeterTier {
		BASIC(5000000, 2000), // 800 J
		ADVANCED(20000000, 8000), // 3200 J
		ELITE(80000000, 32000), // 12800 J
		ULTIMATE(320000000, 128000), // 51200 J
		CREATIVE(Integer.MAX_VALUE, Integer.MAX_VALUE);

		private final int maxEnergy;
		private final int maxTransfer;

		ElectricityMeterTier(int maxEnergy, int maxTransfer) {
			this.maxEnergy = maxEnergy;
			this.maxTransfer = maxTransfer;
		}

		public BaseTier getBaseTier() {
			return BaseTier.values()[ordinal()];
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

        public static ElectricityMeterTier getFromMachineType(MachineType machineType) {
            return values()[machineType.getMetadata()];
        }
	}
}