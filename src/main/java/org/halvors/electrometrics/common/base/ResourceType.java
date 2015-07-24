package org.halvors.electrometrics.common.base;

public enum ResourceType {
	GUI("gui"),
	GUI_ELEMENT("gui/elements"),
	SOUND("sound"),
	TEXTURE_BLOCKS("textures/blocks"),
	TEXTURE_ITEMS("textures/items");

	private final String prefix;

	ResourceType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}