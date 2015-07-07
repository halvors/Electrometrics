package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.base.ElectricityMeterTier;
import org.halvors.electrometrics.common.base.tile.IActiveState;
import org.halvors.electrometrics.common.base.tile.IRotatable;
import org.halvors.electrometrics.common.util.Orientation;
import org.halvors.electrometrics.common.util.render.DefaultIcon;
import org.halvors.electrometrics.common.util.render.Renderer;

import java.util.List;

public class BlockTextured extends BlockBasic {
	@SideOnly(Side.CLIENT)
	private final IIcon[][] iconList = new IIcon[16][16];

	BlockTextured(String name, Material material) {
		super(name, material);

		setBlockName(name);
	}

    @Override
    public int damageDropped (int meta) {
        return meta;
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
        DefaultIcon defaultIcon = DefaultIcon.getAll(baseIcon);

		Renderer.loadDynamicTextures(iconRegister, "BasicElectricityMeter", iconList[0], defaultIcon);
        Renderer.loadDynamicTextures(iconRegister, "AdvancedElectricityMeter", iconList[1], defaultIcon);
        Renderer.loadDynamicTextures(iconRegister, "EliteElectricityMeter", iconList[2], defaultIcon);
        Renderer.loadDynamicTextures(iconRegister, "UltimateElectricityMeter", iconList[3], defaultIcon);
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

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativetabs, List list) {
        for (ElectricityMeterTier tier : ElectricityMeterTier.values()) {
            list.add(new ItemStack(item, 1, tier.ordinal()));
        }
    }
}
