package org.halvors.electrometrics.common.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * When extended, this makes a TileEntity able to provide electricity.
 *
 * @author halvors
 */
public class TileEntityEnergyProvider extends TileEntityEnergyReceiver implements IEnergyProvider {
	public TileEntityEnergyProvider(int maxEnergy) {
		super(maxEnergy);
	}

	public TileEntityEnergyProvider(int maxEnergy, int maxReceive) {
		super(maxEnergy, maxReceive);
	}

	public TileEntityEnergyProvider(int maxEnergy, int maxReceive, int maxExtract) {
		super(maxEnergy, maxReceive, maxExtract);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote) {
			if (storage.getEnergyStored() > 0) {
				transferEnergy();
			}
		}
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	/**
	 * Transfer energy to any blocks demanding energy that are connected to
	 * this one.
	 */
	private void transferEnergy() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

			if (tileEntity instanceof IEnergyReceiver) {
				IEnergyReceiver receiver = (IEnergyReceiver) tileEntity;

				extractEnergy(direction.getOpposite(), receiver.receiveEnergy(direction.getOpposite(), storage.getEnergyStored(), false), false);
			}
		}
	}
}
