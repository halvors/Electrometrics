package org.halvors.electrometrics;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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