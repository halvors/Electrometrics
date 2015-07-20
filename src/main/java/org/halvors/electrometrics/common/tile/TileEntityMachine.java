package org.halvors.electrometrics.common.tile;

import org.halvors.electrometrics.common.base.MachineType;

public class TileEntityMachine extends TileEntityRotatable {
    private final MachineType machineType;

    TileEntityMachine(MachineType machineType) {
        super(machineType.getLocalizedName());

        this.machineType = machineType;
    }

    public MachineType getMachineType() {
        return machineType;
    }
}
