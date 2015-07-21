package org.halvors.electrometrics.common.tile.machine;

import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.tile.TileEntityRotatable;

public class TileEntityMachine extends TileEntityRotatable {
    private final MachineType machineType;

    protected TileEntityMachine(MachineType machineType) {
        super(machineType.getLocalizedName());

        this.machineType = machineType;
    }

    public MachineType getMachineType() {
        return machineType;
    }
}
