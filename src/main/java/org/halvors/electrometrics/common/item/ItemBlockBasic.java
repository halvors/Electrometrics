package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.electrometrics.Electrometrics;

public class ItemBlockBasic extends ItemBlock {
    public ItemBlockBasic(Block block) {
        super(block);

        setCreativeTab(Electrometrics.tabElectrometrics);
    }
}
