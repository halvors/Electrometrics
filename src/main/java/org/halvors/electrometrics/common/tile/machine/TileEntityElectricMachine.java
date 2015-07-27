package org.halvors.electrometrics.common.tile.machine;

import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;

/**
 * This is a basic TileEntity that is meant to be extended by other TileEntities.
 *
 * @author halvors
 */
public class TileEntityElectricMachine extends TileEntityMachine {
	// The current and past redstone state.
	protected boolean isPowered;
	protected boolean wasPowered;

	protected TileEntityElectricMachine(MachineType machineType) {
		super(machineType);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		// Update wasPowered to the current isPowered.
		wasPowered = isPowered;
	}

	public void onNeighborChange() {
		if (!worldObj.isRemote) {
			boolean redstonePower = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

			if (isPowered != redstonePower) {
				isPowered = redstonePower;

				PacketHandler.sendToReceivers(new PacketTileEntity(this), this);
			}
		}
	}
}
