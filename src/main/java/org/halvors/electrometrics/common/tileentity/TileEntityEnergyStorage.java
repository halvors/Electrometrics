package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityEnergyStorage extends TileEntityBasic {
    // The internal energy storage.
    protected EnergyStorage storage;

    public TileEntityEnergyStorage(int storage) {
        this.storage = new EnergyStorage(storage);
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
}