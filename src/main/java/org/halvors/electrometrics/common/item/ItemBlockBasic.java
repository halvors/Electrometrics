package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.electrometrics.Electrometrics;

/**
 * This is a basic ItemBlock that is meant to be extended by other ItemBlocks.
 *
 * @author halvors
 */
class ItemBlockBasic extends ItemBlock {
	ItemBlockBasic(Block block) {
		super(block);

		setCreativeTab(Electrometrics.getTab());
	}
}
