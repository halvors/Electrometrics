package org.halvors.electrometrics.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntityElectricityMeter;

public class TileEntityElectricityMeter extends TileEntityEnergyProvider {
	// The amount of energy that has passed thru.
	private double electricityCount;

	public TileEntityElectricityMeter() {
		super(1024);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		electricityCount = nbtTags.getDouble("electricityCount");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		nbtTags.setDouble("electricityCount", electricityCount);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		// Add the amount of energy we're extracting to the counter.
		if (!simulate) {
			electricityCount += maxExtract;
		}

		return super.extractEnergy(from, maxExtract, simulate);
	}

	public void sync() {
		// Reguest information from the server.
		PacketHandler.getNetwork().sendToServer(new PacketTileEntityElectricityMeter(xCoord, yCoord, zCoord));
	}

	/**
	 * Returns the amount of energy that this block has totally received.
	 */
	public double getElectricityCount() {
		return electricityCount;
	}

	public void setElectricityCount(double electricityCount) {
		this.electricityCount = electricityCount;
	}
}