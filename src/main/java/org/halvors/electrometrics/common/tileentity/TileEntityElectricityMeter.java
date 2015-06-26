package org.halvors.electrometrics.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketElectricityMeter;
import org.halvors.electrometrics.common.network.PacketType;
import org.halvors.electrometrics.common.util.Location;

/**
 * This is the TileEntity of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class TileEntityElectricityMeter extends TileEntityEnergyProvider {
	// The amount of energy that has passed thru.
	private double electricityCount;

	public TileEntityElectricityMeter() {
		super(25600, 25600, 25600);
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

	/*
	 * Request the most recent data from server.
	 */
	public void requestData() {
		PacketHandler.getNetwork().sendToServer(new PacketElectricityMeter(PacketType.GET, new Location(worldObj, xCoord, yCoord, zCoord)));
	}

	/*
	 * Sends the most recent data to the server, this usually happen after use alterations (GUI).
	 */
	public void sendData() {
		PacketHandler.getNetwork().sendToServer(new PacketElectricityMeter(PacketType.SET, new Location(worldObj, xCoord, yCoord, zCoord), electricityCount));
	}

	/**
	 * Returns the amount of energy that this block has totally received.
	 */
	public double getElectricityCount() {
		return electricityCount;
	}

	/*
	 * Sets the amount of energy that this block has totally received.
	 */
	public void setElectricityCount(double electricityCount) {
		this.electricityCount = electricityCount;
	}
}