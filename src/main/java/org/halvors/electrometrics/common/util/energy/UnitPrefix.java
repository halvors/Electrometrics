package org.halvors.electrometrics.common.util.energy;

/**
 * Metric system of measurement.
 */
public enum UnitPrefix {
    MICRO("Micro", "u", 0.000001),
    MILLI("Milli", "m", 0.001),
    BASE("", "", 1),
    KILO("Kilo", "k", 1000),
    MEGA("Mega", "M", 1000000),
    GIGA("Giga", "G", 1000000000),
    TERA("Tera", "T", 1000000000000d),
    PETA("Peta", "P", 1000000000000000d),
    EXA("Exa", "E", 1000000000000000000d),
    ZETTA("Zetta", "Z", 1000000000000000000000d),
    YOTTA("Yotta", "Y", 1000000000000000000000000d);

    /** long name for the unit */
    private final String name;
    /** short unit version of the unit */
    private final String symbol;
    /** Point by which a number is consider to be of this unit */
    private final double value;

    UnitPrefix(String name, String symbol, double value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName(boolean getShort) {
        if (getShort) {
            return symbol;
        } else {
            return name;
        }
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    /** Divides the value by the unit value start */
    public double process(double value) {
        return value / this.value;
    }

    /** Checks if a value is above the unit value start */
    public boolean isAbove(double value) {
        return value > this.value;
    }

    /** Checks if a value is lower than the unit value start */
    public boolean isBellow(double value) {
        return value < this.value;
    }
}