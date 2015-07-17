package org.halvors.electrometrics.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.halvors.electrometrics.client.key.Key;
import org.halvors.electrometrics.client.key.KeyHandler;
import org.halvors.electrometrics.common.base.Tier;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.LanguageUtils;
import org.halvors.electrometrics.common.util.energy.EnergyUtils;
import org.halvors.electrometrics.common.util.render.Color;

import java.util.List;

/**
 * This is the ItemBlock of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class ItemBlockElectricityMeter extends ItemBlockMachine {
	public ItemBlockElectricityMeter(Block block) {
		super(block);
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityplayer, List list, boolean flag) {
		if (!KeyHandler.getIsKeyPressed(Key.SNEAK.getKeyBinding())) {
			list.add(LanguageUtils.translate("tooltip.hold") + " " + Color.AQUA + GameSettings.getKeyDisplayString(Key.SNEAK.getKeyBinding().getKeyCode()) + Color.GREY + " " + LanguageUtils.translate("tooltip.forDetails") + ".");
		} else {
			list.add(Color.BRIGHT_GREEN + LanguageUtils.translate("tooltip.measuredEnergy") + ": " + Color.GREY + EnergyUtils.getEnergyDisplay(getElectricityCount(itemStack)));
			list.add(Color.AQUA + LanguageUtils.translate("tooltip.storedEnergy") + ": " + Color.GREY + EnergyUtils.getEnergyDisplay(getElectricityStored(itemStack)));
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		if (placed) {
			TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

			if (tileEntity instanceof TileEntityElectricityMeter) {
				TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;
				tileEntityElectricityMeter.setTier(getTier(itemStack));
				tileEntityElectricityMeter.setElectricityCount(getElectricityCount(itemStack));
				tileEntityElectricityMeter.getStorage().setEnergyStored(getElectricityStored(itemStack));
			}
		}

		return placed;
	}

	public Tier.ElectricityMeter getTier(ItemStack itemStack) {
		if (itemStack.stackTagCompound != null) {
			int tier = itemStack.stackTagCompound.getInteger("tier");

			return Tier.ElectricityMeter.values()[tier];
		}

		return Tier.ElectricityMeter.BASIC;
	}

	public void setTier(ItemStack itemStack, Tier.ElectricityMeter tier) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setInteger("tier", tier.getBaseTier().ordinal());
	}

	private double getElectricityCount(ItemStack itemStack) {
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

	private int getElectricityStored(ItemStack itemStack) {
		if (itemStack.stackTagCompound != null) {
			return itemStack.stackTagCompound.getInteger("electricityStored");
		}

		return 0;
	}

	public void setElectricityStored(ItemStack itemStack, int electricityStored) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setInteger("electricityStored", electricityStored);
	}
}