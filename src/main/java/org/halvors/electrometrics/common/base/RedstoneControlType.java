package org.halvors.electrometrics.common.base;

public enum RedstoneControlType {
	DISABLED("Disabled"),
	HIGH("High"),
	LOW("Low"),
	PULSE("Pulse");

	private final String display;

	public String getDisplay() {
		return display;
	}

	RedstoneControlType(String s) {
		display = s;
	}
}