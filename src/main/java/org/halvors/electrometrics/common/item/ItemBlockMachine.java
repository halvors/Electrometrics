package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.common.util.Utils;

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
    public String getItemStackDisplayName(ItemStack itemStack) {
        return Utils.translate("tile." + block.getName() + ".name");
    }
}
