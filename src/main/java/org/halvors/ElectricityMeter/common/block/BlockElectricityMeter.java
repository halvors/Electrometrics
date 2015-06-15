package org.halvors.ElectricityMeter.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.ElectricityMeter.ElectricityMeter;
import org.halvors.ElectricityMeter.Reference;
import org.halvors.ElectricityMeter.common.tileentity.TileEntityElectricityMeter;

public class BlockElectricityMeter extends BlockContainer {
	@SideOnly(Side.CLIENT)
	public static IIcon topIcon, sideIcon, bottomIcon;

	public BlockElectricityMeter() {
		super(Material.iron);

		setBlockName("blockElectricityMeter");
		setHardness(3.5F);
		setResistance(8F);
		//setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F);
		setCreativeTab(ElectricityMeter.tabElectricityMeter);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityElectricityMeter();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		topIcon = iconRegister.registerIcon(Reference.PREFIX
				+ "blockElectricityMeter_top");
		sideIcon = iconRegister.registerIcon(Reference.PREFIX
				+ "blockElectricityMeter_side");
		bottomIcon = iconRegister.registerIcon(Reference.PREFIX
				+ "blockElectricityMeter_bottom");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch (side) {
		case 1:
			return topIcon;

		case 0:
			return bottomIcon;

		default:
			return sideIcon;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityElectricityMeter) {
			TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

			player.addChatMessage(new ChatComponentText("[ElectricityMeter]"));
			player.addChatMessage(new ChatComponentText("A total of " + tileEntityElectricityMeter.getElectricityCount() + " has passed thru."));
			player.addChatMessage(new ChatComponentText("A total of " + tileEntityElectricityMeter.getEnergy() + " is stored."));

			return true;
		}

		return false;
	}
}
