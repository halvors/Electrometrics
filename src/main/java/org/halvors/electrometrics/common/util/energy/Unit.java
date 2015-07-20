package org.halvors.electrometrics.common.util.energy;

/**
 * Universal Electricity's units are in KILOJOULES, KILOWATTS and KILOVOLTS. Try to make your
 * energy ratio as close to real life as possible.
 */
public enum Unit {
		/*
		AMPERE("Amp", "I"),
		AMP_HOUR("Amp Hour", "Ah"),
		VOLTAGE("Volt", "V"),
		WATT("Watt", "W"),
		WATT_HOUR("Watt Hour", "Wh"),
		RESISTANCE("Ohm", "R"),
		CONDUCTANCE("Siemen", "S"),
		JOULES("Joule", "J"),
		LITER("Liter", "L"),
		NEWTON_METER("Newton Meter", "Nm"),
		REDFLUX("Redstone-Flux", "Rf"),
		MINECRAFT_JOULES("Minecraft-Joules", "Mj"),
		ELECTRICAL_UNITS("Electrical-Units", "Eu");
		*/

    REDSTONE_FLUX("Redstone Flux", "RF"),
    JOULES("Joule", "J"),
    MINECRAFT_JOULES("Minecraft Joule", "MJ"),
    ELECTRICAL_UNITS("Electrical Unit", "EU");

    private final String name;
    private final String symbol;

    Unit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPlural() {
        return this.name + "s";
    }
}