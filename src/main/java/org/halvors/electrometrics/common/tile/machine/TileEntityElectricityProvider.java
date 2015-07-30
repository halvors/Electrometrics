package org.halvors.electrometrics.common.tile.machine;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.util.MachineUtils;

import java.util.EnumSet;

/**
 * When extended, this makes a TileEntity able to provide electricity.
 *
 * @author halvors
 */
public class TileEntityElectricityProvider extends TileEntityElectricityReceiver implements IEnergyProvider {
	protected TileEntityElectricityProvider(MachineType machineType, int maxEnergy) {
		super(machineType, maxEnergy);
	}

	protected TileEntityElectricityProvider(MachineType machineType, int maxEnergy, int maxTransfer) {
		super(machineType, maxEnergy, maxTransfer);
	}

	protected TileEntityElectricityProvider(MachineType machineType, int maxEnergy, int maxReceive, int maxExtract) {
		super(machineType, maxEnergy, maxReceive, maxExtract);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote) {
			if (MachineUtils.canFunction(this)) {
				distributeEnergy();
			}
		}
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return getReceivingDirections().contains(from) || getExtractingDirections().contains(from);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (getExtractingDirections().contains(from)) {
			return storage.extractEnergy(maxExtract, simulate);
		}

		return 0;
	}

	protected EnumSet<ForgeDirection> getExtractingDirections() {
		return EnumSet.noneOf(ForgeDirection.class);
	}

	/**
	 * Transfer energy to any blocks demanding energy that are connected to
	 * this one.
	 */
	protected void distributeEnergy() {
		for (ForgeDirection direction : getExtractingDirections()) {
			TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

			if (tileEntity instanceof IEnergyReceiver) {
				IEnergyReceiver receiver = (IEnergyReceiver) tileEntity;
				int actualEnergyAmount = extractEnergy(direction, getExtract(), true);

				if (actualEnergyAmount > 0) {
					extractEnergy(direction, receiver.receiveEnergy(direction.getOpposite(), actualEnergyAmount, false), false);
				}
			}
		}
	}
}
