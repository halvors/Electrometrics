package org.halvors.electrometrics.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.Color;
import org.halvors.electrometrics.common.util.Utils;

import java.util.List;

/**
 * This is the ItemBlock of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class ItemBlockElectricityMeter extends ItemBlockBasic {
	public ItemBlockElectricityMeter(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityplayer, List list, boolean flag) {
		list.add(Color.BRIGHT_GREEN + "Measured energy: " + Color.GREY + Utils.getEnergyDisplay(getElectricityCount(itemStack)));
	}

	@Override
	public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		if (placed) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			if (tileEntity instanceof TileEntityElectricityMeter) {
				TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;
				tileEntityElectricityMeter.setElectricityCount(getElectricityCount(itemStack));
			}
		}

		return placed;
	}

	public double getElectricityCount(ItemStack itemStack) {
		if (itemStack.stackTagCompound != null) {
			return itemStack.stackTagCompound.getDouble("electricityCount");
		}

		return 0;
	}

	public void setElectricityCount(ItemStack itemStack, double electricityCount) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setDouble("electricityCount", electricityCount);
	}
}