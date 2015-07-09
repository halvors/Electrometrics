package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.block.BlockContainer;

/**
 * This is a basic ItemBlock that is meant to be extended by other ItemBlocks.
 *
 * @author halvors
 */
class ItemBlockContainer extends ItemBlock {
	final BlockContainer block;

	ItemBlockContainer(Block block) {
		super(block);

		this.block = (BlockContainer) block;

		setCreativeTab(Electrometrics.getTab());
	}
}
