package org.halvors.electrometrics.common.tile.machine;

import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.ConfigurationManager.General;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.tile.TileEntityRotatable;
import org.halvors.electrometrics.common.util.location.BlockLocation;

public class TileEntityMachine extends TileEntityRotatable {
	private final MachineType machineType;

	protected TileEntityMachine(MachineType machineType) {
		super(machineType.getLocalizedName());

		this.machineType = machineType;
	}

    @Override
    public void updateEntity() {
        super.updateEntity();

        // Remove disabled blocks.
        if (!worldObj.isRemote && General.destroyDisabledBlocks) {
            MachineType machineType = MachineType.getType(getBlockType(), getBlockMetadata());

            if (machineType != null && !machineType.isEnabled()) {
                Electrometrics.getLogger().info("Destroying machine of type '" + machineType.getLocalizedName() + "' at " + new BlockLocation(this) + " as according to configuration.");
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
        }
    }

	public MachineType getMachineType() {
		return machineType;
	}
}
