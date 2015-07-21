package org.halvors.electrometrics.common.base.tile;

public enum RedstoneControllableType {
	DISABLED("Disabled"),
	HIGH("High"),
	LOW("Low"),
	PULSE("Pulse");

	private final String display;

	public String getDisplay() {
		return display;
	}

	RedstoneControllableType(String s) {
		display = s;
	}
}