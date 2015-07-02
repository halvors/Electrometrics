package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public abstract class TileEntityEnergyStorage extends TileEntityMachine implements INetworkable {
	// The internal energy storage.
	final EnergyStorage storage;

	TileEntityEnergyStorage(String name, int maxEnergy) {
		super(name);

		storage = new EnergyStorage(maxEnergy);
	}

	TileEntityEnergyStorage(String name, int maxEnergy, int maxReceive) {
		this(name, maxEnergy);

		storage.setMaxReceive(maxReceive);
	}

	TileEntityEnergyStorage(String name, int maxEnergy, int maxReceive, int maxExtract) {
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
	public List<Object> getPacketData(List<Object> list) {
		super.getPacketData(list);

        list.add(storage.getEnergyStored());

		return list;
	}

	public EnergyStorage getStorage() {
		return storage;
	}
}