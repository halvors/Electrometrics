package org.halvors.electrometrics.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;

/**
 * This is a basic Block that is meant to be extended by other Blocks.
 *
 * @author halvors
 */
public class BlockBasic extends BlockContainer {
	BlockBasic(Material material) {
		super(material);

		setCreativeTab(Electrometrics.getTabElectrometrics());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}
}