package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

/**
 * When extended, this makes a TileEntity able to receive electricity.
 *
 * @author halvors
 */
public abstract class TileEntityEnergyReceiver extends TileEntityEnergyStorage implements IEnergyReceiver {
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
		if (getReceivingSides().contains(from)) {
			return storage.receiveEnergy(maxReceive, simulate);
		}

		return 0;
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
		return getReceivingSides().contains(from) || getExtractingSides().contains(from);
	}

	public EnumSet<ForgeDirection> getReceivingSides() {
		EnumSet<ForgeDirection> directions = EnumSet.allOf(ForgeDirection.class);
		directions.remove(ForgeDirection.UNKNOWN);

		return directions;
	}

	public EnumSet<ForgeDirection> getExtractingSides() {
		return EnumSet.noneOf(ForgeDirection.class);
	}
}