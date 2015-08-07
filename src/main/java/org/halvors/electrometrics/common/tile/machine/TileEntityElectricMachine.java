package org.halvors.electrometrics.common.tile.machine;

import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.tile.component.TileRedstoneControlComponent;

/**
 * This is a basic TileEntity that is meant to be extended by other TileEntities.
 *
 * @author halvors
 */
public class TileEntityElectricMachine extends TileEntityMachine {
	protected TileEntityElectricMachine(MachineType machineType) {
		super(machineType);

		components.add(new TileRedstoneControlComponent(this));
	}
}
