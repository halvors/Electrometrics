package org.halvors.electrometrics.common.item;

import net.minecraft.block.Block;

public class ItemBlockSubtyped extends ItemBlock {
	protected ItemBlockSubtyped(Block block) {
		super(block);

		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
}
