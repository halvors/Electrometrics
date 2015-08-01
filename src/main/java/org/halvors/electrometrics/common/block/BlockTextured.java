package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import org.halvors.electrometrics.client.render.DefaultIcon;
import org.halvors.electrometrics.common.Reference;

public abstract class BlockTextured extends Block {
	protected final IIcon[] iconList = new IIcon[16];

	protected DefaultIcon defaultBlockIcon;

	protected BlockTextured(String name, Material material) {
		super(name, material);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.PREFIX + name);
		defaultBlockIcon = DefaultIcon.getAll(blockIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return iconList[metadata];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}
}