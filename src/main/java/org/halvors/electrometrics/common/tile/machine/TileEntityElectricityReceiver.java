package org.halvors.electrometrics.common.tile.machine;

import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.base.MachineType;

import java.util.EnumSet;

/**
 * When extended, this makes a TileEntity able to receive electricity.
 *
 * @author halvors
 */
public abstract class TileEntityElectricityReceiver extends TileEntityElectricityStorage implements IEnergyReceiver {
	protected TileEntityElectricityReceiver(MachineType machineType, int maxEnergy) {
		super(machineType, maxEnergy);
	}

	protected TileEntityElectricityReceiver(MachineType machineType, int maxEnergy, int maxTransfer) {
		super(machineType, maxEnergy, maxTransfer);
	}

	protected TileEntityElectricityReceiver(MachineType machineType, int maxEnergy, int maxReceive, int maxExtract) {
		super(machineType, maxEnergy, maxReceive, maxExtract);
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

	EnumSet<ForgeDirection> getReceivingSides() {
		EnumSet<ForgeDirection> directions = EnumSet.allOf(ForgeDirection.class);
		directions.remove(ForgeDirection.UNKNOWN);

		return directions;
	}

	EnumSet<ForgeDirection> getExtractingSides() {
		return EnumSet.noneOf(ForgeDirection.class);
	}
}