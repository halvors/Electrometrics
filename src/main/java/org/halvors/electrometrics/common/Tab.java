package org.halvors.electrometrics.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.Reference;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class Tab extends CreativeTabs {
	public Tab() {
		super("tab" + Reference.NAME);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Electrometrics.blockElectricityMeter);
	}

	@Override
	public Item getTabIconItem() {
		return new ItemBlock(Electrometrics.blockElectricityMeter);
	}
}