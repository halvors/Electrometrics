package org.halvors.electrometrics.common.tile;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;

import java.util.List;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public abstract class TileEntityElectricityStorage extends TileEntityElectricBlock implements ITileNetworkable {
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
    public void validate() {
        super.validate();

        PacketHandler.sendToServer(new PacketRequestData(this));
    }

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		storage.readFromNBT(nbtTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		storage.writeToNBT(nbtTagCompound);
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		storage.setEnergyStored(dataStream.readInt());
	}

	@Override
	public List<Object> getPacketData(List<Object> objects) {
		super.getPacketData(objects);

		objects.add(storage.getEnergyStored());

		return objects;
	}

	public EnergyStorage getStorage() {
		return storage;
	}
}