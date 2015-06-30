package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public abstract class TileEntityEnergyStorage extends TileEntityMachine implements INetworkable {
	// The internal energy storage.
	protected final EnergyStorage storage;

	public TileEntityEnergyStorage(String name, int maxEnergy) {
		super(name);

		storage = new EnergyStorage(maxEnergy);
	}

	public TileEntityEnergyStorage(String name, int maxEnergy, int maxReceive) {
		this(name, maxEnergy);

		storage.setMaxReceive(maxReceive);
	}

	public TileEntityEnergyStorage(String name, int maxEnergy, int maxReceive, int maxExtract) {
		this(name, maxEnergy, maxReceive);

		storage.setMaxExtract(maxExtract);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		storage.readFromNBT(nbtTags);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		storage.writeToNBT(nbtTags);
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		storage.setEnergyStored(dataStream.readInt());
	}

	@Override
	public ArrayList<Object> getPacketData(ArrayList<Object> data) {
		super.getPacketData(data);

		data.add(storage.getEnergyStored());

		return data;
	}

	public EnergyStorage getStorage() {
		return storage;
	}
}