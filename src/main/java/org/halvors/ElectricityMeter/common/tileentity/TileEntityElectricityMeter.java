package org.halvors.ElectricityMeter.common.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityElectricityMeter extends TileEntity implements IEnergyProvider, IEnergyReceiver {
	private EnergyStorage storage = new EnergyStorage(64000);

	/* The amount of energy that has passed thru. */
	private double electricityCount;

	public TileEntityElectricityMeter() {

	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote) {
			if (storage.getEnergyStored() > 0) {
				transferEnergy();
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);
		storage.readFromNBT(nbtTags);

		electricityCount = nbtTags.getDouble("electricityCount");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);
		storage.writeToNBT(nbtTags);

		nbtTags.setDouble("electricityCount", electricityCount);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		electricityCount += maxReceive;

		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	/**
	 * Returns the amount of energy that this block has totally received.
	 */
	public double getElectricityCount() {
		return electricityCount;
	}

	/**
	 * Transfer energy to any blocks demanding energy that are connected to
	 * this one.
	 */
	private void transferEnergy() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

			if (tileEntity instanceof IEnergyReceiver) {
				IEnergyReceiver receiver = (IEnergyReceiver) tileEntity;
				extractEnergy(direction.getOpposite(), receiver.receiveEnergy(direction.getOpposite(), storage.getEnergyStored(), false), false);
			}
		}
	}
}