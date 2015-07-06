package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.base.tile.IActiveState;
import org.halvors.electrometrics.common.base.tile.IRotatable;
import org.halvors.electrometrics.common.util.Orientation;
import org.halvors.electrometrics.common.util.render.DefaultIcon;
import org.halvors.electrometrics.common.util.render.Renderer;

public class BlockTextured extends BlockBasic {
	@SideOnly(Side.CLIENT)
	private final IIcon[][] iconList = new IIcon[16][16];

	BlockTextured(String name, Material material) {
		super(name, material);

		setBlockName(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		IIcon baseIcon = iconRegister.registerIcon(Reference.PREFIX + name);

		Renderer.loadDynamicTextures(iconRegister, name, iconList[0], DefaultIcon.getAll(baseIcon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		boolean isActive = false;

		// Check if this implements IActiveState, if it do we get the state from it.
		if (tileEntity instanceof IActiveState) {
			IActiveState activeState = (IActiveState) tileEntity;

			isActive = activeState.isActive();
		}

		// Check if this implements IRotatable.
		if (tileEntity instanceof IRotatable) {
			IRotatable rotatable = (IRotatable) tileEntity;

			return iconList[meta][Orientation.getBaseOrientation(side, rotatable.getFacing()) + (isActive ? 6 : 0)];
		}

		return iconList[meta][side + (isActive ? 6 : 0)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		// Workaround to for when block is not rendered in world, by swapping the front and back sides.
		switch (side) {
			case 2: // Back
				side = 3;
				break;

			case 3: // Front
				side = 2;
				break;
		}

		return iconList[meta][side];
	}
}
