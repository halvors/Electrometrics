package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.util.Utils;

public class ItemBlockMachine extends ItemBlockContainer {
    final MachineType machineType = MachineType.getType(new ItemStack(this));

    ItemBlockMachine(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        switch (machineType) {
            case BASIC_ELECTRICITY_METER:
            case ADVANCED_ELECTRICITY_METER:
            case ELITE_ELECTRICITY_METER:
            case ULTIMATE_ELECTRICITY_METER:
            case CREATIVE_ELECTRICITY_METER:
                if (this instanceof ItemBlockElectricityMeter) {
                    ItemBlockElectricityMeter itemBlockElectricityMeter = (ItemBlockElectricityMeter) this;

                    return Utils.translate("tile." + itemBlockElectricityMeter.getTier(itemStack).getBaseTier().getName() + block.getName() + ".name");
                }

            default:
                return super.getItemStackDisplayName(itemStack);
        }
    }
}
