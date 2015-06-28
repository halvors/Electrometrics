package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * When extended, this makes a TileEntity able to receive electricity.
 *
 * @author halvors
 */
public class TileEntityEnergyReceiver extends TileEntityEnergyStorage implements IEnergyReceiver {
	public TileEntityEnergyReceiver(int maxEnergy) {
		super(maxEnergy);
	}

	public TileEntityEnergyReceiver(int maxEnergy, int maxReceive) {
		super(maxEnergy, maxReceive);
	}

	public TileEntityEnergyReceiver(int maxEnergy, int maxReceive, int maxExtract) {
		super(maxEnergy, maxReceive, maxExtract);
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