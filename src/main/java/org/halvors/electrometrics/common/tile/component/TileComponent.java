package org.halvors.electrometrics.common.tile.component;

import org.halvors.electrometrics.common.tile.TileEntity;

public class TileComponent {
    private final TileEntity tileEntity;

    public TileComponent(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }
}
