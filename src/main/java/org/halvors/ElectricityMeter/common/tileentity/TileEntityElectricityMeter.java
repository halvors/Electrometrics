package org.halvors.ElectricityMeter.common.tileentity;

import mekanism.api.Coord4D;
import mekanism.api.Range4D;
import mekanism.api.energy.ICableOutputter;
import mekanism.api.energy.IStrictEnergyAcceptor;
import mekanism.api.energy.IStrictEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class TileEntityElectricityMeter extends TileEntity implements IStrictEnergyAcceptor, IStrictEnergyStorage, ICableOutputter {
	/* The amount of energy that has passed thru. */
	private double electricityCount;

	/* How much energy is stored in this block. */
	private double electricityStored;

	/* Actual maximum energy storage */
	private double maxEnergy = 1000000000;

	public TileEntityElectricityMeter() {
		
	}

	public double getElectricityCount() {
		return electricityCount;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!worldObj.isRemote)
		{
			ChargeUtils.charge(0, this);
			ChargeUtils.discharge(1, this);

			if(MekanismUtils.canFunction(this))
			{
				CableUtils.emit(this);
			}

			int newScale = getScaledEnergyLevel(20);

			if(newScale != prevScale)
			{
				Mekanism.packetHandler.sendToReceivers(new TileEntityMessage(Coord4D.get(this), getNetworkedData(new ArrayList())), new Range4D(Coord4D.get(this)));
			}

			prevScale = newScale;
		}
	}

	@Override
	public boolean canOutputTo(ForgeDirection side) {
		return true;
	}

	@Override
	public double transferEnergyToAcceptor(ForgeDirection side, double amount) {
		double toUse = Math.min(getMaxEnergy() - getEnergy(), amount);
		setEnergy(getEnergy() + toUse);

		return toUse;
	}

	@Override
	public boolean canReceiveEnergy(ForgeDirection side) {
		return true;
	}

	@Override
	public double getEnergy() {
		return electricityStored;
	}

	@Override
	public void setEnergy(double energy) {
		electricityStored = Math.max(Math.min(energy, getMaxEnergy()), 0);
	}

	@Override
	public double getMaxEnergy() {
		return maxEnergy;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		electricityCount = nbtTags.getDouble("electricityCount");
		electricityStored = nbtTags.getDouble("electricityStored");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		nbtTags.setDouble("electricityCount", electricityCount);
		nbtTags.setDouble("electricityStored", electricityStored);
	}
}