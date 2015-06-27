package org.halvors.electrometrics.common.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
public class TileEntityElectricityMeter extends TileEntityEnergyProvider implements INetworkable, IActiveState {
	// The amount of energy that has passed thru.
	private double electricityCount;

	// Whether or not this block is in it's active state.
	private boolean isActive = false;

	// The client's current active state.
	@SideOnly(Side.CLIENT)
	public boolean clientIsActive;

	public TileEntityElectricityMeter() {
		super(25600, 25600, 25600);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		electricityCount = nbtTags.getDouble("electricityCount");
		isActive = nbtTags.getBoolean("isActive");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		nbtTags.setDouble("electricityCount", electricityCount);
		nbtTags.setBoolean("isActive", isActive);
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

		setElectricityCount(dataStream.readDouble());
		setActive(dataStream.readBoolean());

		// Check if client is in sync with the server, if not update it.
		if (clientIsActive != isActive) {
			clientIsActive = isActive;

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public ArrayList getPacketData(ArrayList data) {
		super.getPacketData(data);

		data.add(getElectricityCount());
		data.add(isActive());

		return data;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
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