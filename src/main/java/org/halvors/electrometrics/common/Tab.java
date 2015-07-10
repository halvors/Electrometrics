package org.halvors.electrometrics.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.base.MachineType;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class Tab extends CreativeTabs {
    private final MachineType machineType = MachineType.BASIC_ELECTRICITY_METER;

	public Tab() {
		super("tab" + Reference.NAME);
	}

	@Override
	public ItemStack getIconItemStack() {
		return machineType.getItemStack();
	}

    @Override
    public Item getTabIconItem() {
        return machineType.getItem();
    }
}