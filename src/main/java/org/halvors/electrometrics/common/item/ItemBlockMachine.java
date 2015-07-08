package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;

public class ItemBlockMachine extends ItemBlockContainer {
    ItemBlockMachine(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    /*
    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return Utils.translate("tile." + block.getName() + ".name");
    }
    */
}
