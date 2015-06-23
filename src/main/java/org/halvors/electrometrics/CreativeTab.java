package org.halvors.electrometrics;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    public CreativeTab() {
        super("tabElectroMetrics");
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ElectroMetrics.blockElectricityMeter);
    }

    @Override
    public Item getTabIconItem() {
        return new ItemBlock(ElectroMetrics.blockElectricityMeter);
    }
}