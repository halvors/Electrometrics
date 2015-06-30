package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * When extended, this makes a TileEntity able to receive electricity.
 *
 * @author halvors
 */
abstract class TileEntityEnergyReceiver extends TileEntityEnergyStorage implements IEnergyReceiver {
	TileEntityEnergyReceiver(String name, int maxEnergy) {
		super(name, maxEnergy);
	}

	TileEntityEnergyReceiver(String name, int maxEnergy, int maxReceive) {
		super(name, maxEnergy, maxReceive);
	}

	TileEntityEnergyReceiver(String name, int maxEnergy, int maxReceive, int maxExtract) {
		super(name, maxEnergy, maxReceive, maxExtract);
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}
}