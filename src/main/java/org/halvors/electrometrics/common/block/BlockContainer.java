package org.halvors.electrometrics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;

/**
 * This is a basic Block that is meant to be extended by other Blocks.
 *
 * @author halvors
 */
public class BlockContainer extends net.minecraft.block.BlockContainer {
	protected final String name;

	BlockContainer(String name, Material material) {
		super(material);

		this.name = name;

		setCreativeTab(Electrometrics.getTab());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}

	public String getName() {
		return name;
	}
}