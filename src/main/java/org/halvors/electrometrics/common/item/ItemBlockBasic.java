package org.halvors.electrometrics.common.item;

import net.minecraft.item.ItemBlock;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.block.BlockBasic;

/**
 * This is a basic ItemBlock that is meant to be extended by other ItemBlocks.
 *
 * @author halvors
 */
class ItemBlockBasic extends ItemBlock {
	ItemBlockBasic(BlockBasic block) {
		super(block);

		setCreativeTab(Electrometrics.getTab());
	}
}
