package org.halvors.electrometrics.common.base;

import net.minecraft.util.StatCollector;
import org.halvors.electrometrics.common.util.Color;

public enum Tier {
	BASIC("Basic", Color.BRIGHT_GREEN),
	ADVANCED("Advanced", Color.DARK_RED),
	ELITE("Elite", Color.DARK_BLUE),
	ULTIMATE("Ultimate", Color.PURPLE),
	CREATIVE("Creative", Color.BLACK);

	private final String name;
	private final Color color;

	Tier(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getLocalizedName() {
		return StatCollector.translateToLocal("tier." + getName());
	}

	public Color getColor() {
		return color;
	}

	public boolean isObtainable() {
		return this != CREATIVE;
	}
}