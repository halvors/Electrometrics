package org.halvors.electrometrics.common.util.energy;

/**
 * An easy way to display information on electricity for the client.
 * This class is borrowed from Universal Electricity and is customized to fit our use.
 *
 * @author Calclavia
 */
public class UnitDisplay {
	private static String getDisplay(double value, Unit unit, int decimalPlaces, boolean isShort) {
		return getDisplay(value, unit, decimalPlaces, isShort, 1);
	}

	/**
	 * Displays the unit as text. Does handle negative numbers, and will place a negative sign in
	 * front of the output string showing this. Use string.replace to remove the negative sign if
	 * unwanted
	 */
	private static String getDisplay(double value, Unit unit, int decimalPlaces, boolean isShort, double multiplier) {
		String unitName = unit.getName();
		String prefix = "";

		if (value < 0) {
			value = Math.abs(value);
			prefix = "-";
		}

		value *= multiplier;

		if (isShort) {
			unitName = unit.getSymbol();
		} else if (value > 1) {
			unitName = unit.getPlural();
		}

		if (value == 0) {
			return value + " " + unitName;
		} else {
			for (int i = 0; i < UnitPrefix.values().length; i++) {
				UnitPrefix lowerMeasure = UnitPrefix.values()[i];

				if (lowerMeasure.isBellow(value) && lowerMeasure.ordinal() == 0) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
				}

				if (lowerMeasure.ordinal() + 1 >= UnitPrefix.values().length) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
				}

				UnitPrefix upperMeasure = UnitPrefix.values()[i + 1];

				if ((lowerMeasure.isAbove(value) && upperMeasure.isBellow(value)) || lowerMeasure.getValue() == value) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
				}
			}
		}

		return prefix + roundDecimals(value, decimalPlaces) + " " + unitName;
	}

	public static String getDisplay(double value, Unit unit) {
		return getDisplay(value, unit, 2, false);
	}

	public static String getDisplay(double value, Unit unit, UnitPrefix prefix) {
		return getDisplay(value, unit, 2, false, prefix.getValue());
	}

	public static String getDisplayShort(double value, Unit unit) {
		return getDisplay(value, unit, 2, true);
	}

	/**
	 * Gets a display for the value with a unit that is in the specific prefix.
	 */
	public static String getDisplayShort(double value, Unit unit, UnitPrefix prefix) {
		return getDisplay(value, unit, 2, true, prefix.getValue());
	}

	public static String getDisplayShort(double value, Unit unit, int decimalPlaces) {
		return getDisplay(value, unit, decimalPlaces, true);
	}

	public static String getDisplaySimple(double value, Unit unit, int decimalPlaces) {
		if (value > 1) {
			if (decimalPlaces < 1) {
				return (int) value + " " + unit.getPlural();
			}

			return roundDecimals(value, decimalPlaces) + " " + unit.getPlural();
		}

		if (decimalPlaces < 1) {
			return (int) value + " " + unit.getName();
		}

		return roundDecimals(value, decimalPlaces) + " " + unit.getName();
	}

	/**
	 * Rounds a number to a specific number place places.
	 * @param d The number
	 * @param decimalPlaces The rounded number
	 * @return the rounded number.
	 */
	private static double roundDecimals(double d, int decimalPlaces) {
		int j = (int) (d * Math.pow(10, decimalPlaces));

		return j / Math.pow(10, decimalPlaces);
	}

	public static double roundDecimals(double d) {
		return roundDecimals(d, 2);
	}
}