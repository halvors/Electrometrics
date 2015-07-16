package org.halvors.electrometrics.common.tile;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.tile.INetworkable;

import java.util.List;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public abstract class TileEntityElectricityStorage extends TileEntityElectricBlock implements INetworkable {
	// The internal energy storage.
	final EnergyStorage storage;

	TileEntityElectricityStorage(MachineType machineType, int maxEnergy) {
		super(machineType);

		storage = new EnergyStorage(maxEnergy);
	}

    TileEntityElectricityStorage(MachineType machineType, int maxEnergy, int maxReceive, int maxExtract) {
        this(machineType, maxEnergy);

        storage.setMaxReceive(maxReceive);
        storage.setMaxExtract(maxExtract);
    }

	TileEntityElectricityStorage(MachineType machineType, int maxEnergy, int maxTransfer) {
		this(machineType, maxEnergy);

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