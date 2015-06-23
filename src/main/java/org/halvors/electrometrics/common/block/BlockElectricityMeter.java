package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.UnitDisplay;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

public class BlockElectricityMeter extends BlockBasic {
	@SideOnly(Side.CLIENT)
	public static IIcon icon, frontIcon;

	public BlockElectricityMeter() {
		super(Material.iron);

		setBlockName("blockElectricityMeter");
		setHardness(3.5F);
		setResistance(8F);
		//setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityElectricityMeter();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(Reference.PREFIX + "blockElectricityMeter");
		frontIcon = iconRegister.registerIcon(Reference.PREFIX + "blockElectricityMeterFront");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch (side) {
		case 2:
			return frontIcon;

		default:
			return icon;
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
		if (player.isSneaking()) {
			return false;
		}

		if (world.isRemote) {
			return true;
		}

		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityElectricityMeter) {
			// Open the GUI.
			player.openGui(Electrometrics.instance, 0, world, x, y, z);
		}

		if (tileEntity instanceof TileEntityElectricityMeter) {
			TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

			player.addChatMessage(new ChatComponentText("[Electricity Meter]"));

			for (UnitDisplay.Unit unit : UnitDisplay.Unit.values()) {
				player.addChatMessage(new ChatComponentText("A total of " + UnitDisplay.getDisplayShort(tileEntityElectricityMeter.getElectricityCount(), unit) + " has passed thru."));
			}
		}

		return true;
	}
}
