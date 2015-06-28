package org.halvors.electrometrics.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.Electrometrics;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTab extends CreativeTabs {
	public CreativeTab() {
		super("tabElectrometrics");
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