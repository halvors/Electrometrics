package org.halvors.electrometrics.common.tileentity;

public enum RedstoneControlType {
    DISABLED("Disabled"),
    HIGH("High"),
    LOW("Low"),
    PULSE("Pulse");

    private String display;

    public String getDisplay() {
        return display;
    }

    RedstoneControlType(String s) {
        display = s;
    }
}