package org.halvors.electrometrics.common.tile;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.INetworkable;

import java.util.List;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public abstract class TileEntityElectricityStorage extends TileEntityElectricMachine implements INetworkable {
	// The internal energy storage.
	final EnergyStorage storage;

	TileEntityElectricityStorage(String name, int maxEnergy) {
		super(name);

		storage = new EnergyStorage(maxEnergy);
	}

    TileEntityElectricityStorage(String name, int maxEnergy, int maxReceive, int maxExtract) {
        this(name, maxEnergy);

        storage.setMaxReceive(maxReceive);
        storage.setMaxExtract(maxExtract);
    }

	TileEntityElectricityStorage(String name, int maxEnergy, int maxTransfer) {
		this(name, maxEnergy);

		storage.setMaxTransfer(maxTransfer);
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