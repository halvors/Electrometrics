package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.common.block.BlockBasic;
import org.halvors.electrometrics.common.util.Utils;

public class ItemBlockTextured extends ItemBlockBasic {
    protected final BlockBasic block;

    ItemBlockTextured(Block block) {
        super(block);

        this.block = (BlockBasic) block;

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return Utils.translate("tile." + block.getName() + ".name");
    }
}
