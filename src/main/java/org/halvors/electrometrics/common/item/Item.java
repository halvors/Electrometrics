package org.halvors.electrometrics.common.item;

import org.halvors.electrometrics.Electrometrics;

/**
 * This is a basic Item that is meant to be extended by other Items.
 *
 * @author halvors
 */
public class Item extends net.minecraft.item.Item {
	final String name;

	Item(String name) {
		this.name = name;

		setUnlocalizedName(name);
		setCreativeTab(Electrometrics.getTab());
	}
}
