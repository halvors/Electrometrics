package org.halvors.ElectricityMeter;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    public CreativeTab() {
        super("tabElectricityMeter");
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ElectricityMeter.blockElectricityMeter);
    }

    @Override
    public Item getTabIconItem() {
        return new ItemBlock(ElectricityMeter.blockElectricityMeter);
    }
}