package org.halvors.ElectricityMeter;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabElectricityMeter extends CreativeTabs {
    public CreativeTabElectricityMeter() {
        super("tabElectricityMeter");
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ElectricityMeter.blockElectricityMeter);
    }

    @Override
    public Item getTabIconItem() {
        return Items.clock;
    }
}