package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public class TileEntityEnergyStorage extends TileEntityMachine {
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

    public EnergyStorage getStorage() {
        return storage;
    }
}