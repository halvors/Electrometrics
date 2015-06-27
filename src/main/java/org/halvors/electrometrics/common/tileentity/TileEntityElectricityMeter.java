package org.halvors.electrometrics.common.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

/**
 * This is the TileEntity of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class TileEntityElectricityMeter extends TileEntityEnergyProvider implements INetworkable {
	// The amount of energy that has passed thru.
	private double electricityCount;

	public TileEntityElectricityMeter() {
		super(25600, 25600, 25600);
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

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		electricityCount = dataStream.readDouble();
	}

	@Override
	public ArrayList getPacketData(ArrayList data) {
		super.getPacketData(data);

		data.add(getElectricityCount());

		return data;
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