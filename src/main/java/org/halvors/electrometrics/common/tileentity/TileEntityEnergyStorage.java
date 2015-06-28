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
public class TileEntityEnergyStorage extends TileEntityMachine implements INetworkable {
    // The internal energy storage.
    protected EnergyStorage storage;

    public TileEntityEnergyStorage(int maxEnergy) {
        storage = new EnergyStorage(maxEnergy);
    }

    public TileEntityEnergyStorage(int maxEnergy, int maxReceive) {
        storage = new EnergyStorage(maxEnergy);
        storage.setMaxReceive(maxReceive);
    }

    public TileEntityEnergyStorage(int maxEnergy, int maxReceive, int maxExtract) {
        storage = new EnergyStorage(maxEnergy);
        storage.setMaxReceive(maxReceive);
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