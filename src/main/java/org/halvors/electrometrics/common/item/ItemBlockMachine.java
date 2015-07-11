package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.common.base.MachineType;

public class ItemBlockMachine extends ItemBlockContainer {
    ItemBlockMachine(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        MachineType machineType = MachineType.getType(itemStack);

        return machineType.getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        MachineType machineType = MachineType.getType(itemStack);

        return machineType.getLocalizedName();
    }
}
