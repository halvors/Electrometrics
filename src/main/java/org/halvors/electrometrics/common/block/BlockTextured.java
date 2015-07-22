package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.halvors.electrometrics.client.render.DefaultIcon;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.base.tile.ITileActiveState;
import org.halvors.electrometrics.common.base.tile.ITileRotatable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.render.Orientation;

abstract class BlockTextured extends BlockContainer {
	@SideOnly(Side.CLIENT)
	final IIcon[][] iconList = new IIcon[16][16];

	@SideOnly(Side.CLIENT)
    DefaultIcon defaultBaseIcon;

	BlockTextured(String name, Material material) {
		super(name, material);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		IIcon baseIcon = iconRegister.registerIcon(Reference.PREFIX + name);
		defaultBaseIcon = DefaultIcon.getAll(baseIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		boolean isActive = false;

		// Check if this implements ITileActiveState, if it do we get the state from it.
		if (tileEntity instanceof ITileActiveState) {
			ITileActiveState activeState = (ITileActiveState) tileEntity;

			isActive = activeState.isActive();
		}

		// Check if this implements ITileRotatable.
		if (tileEntity instanceof ITileRotatable) {
			ITileRotatable rotatable = (ITileRotatable) tileEntity;

			return iconList[metadata][Orientation.getBaseOrientation(side, rotatable.getFacing()) + (isActive ? 6 : 0)];
		}

		return iconList[metadata][side + (isActive ? 6 : 0)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		// Workaround to for when block is not rendered in world, by swapping the front and back sides.
		switch (side) {
			case 2: // Back
				side = 3;
				break;

			case 3: // Front
				side = 2;
				break;
		}

		return iconList[metadata][side];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}
}