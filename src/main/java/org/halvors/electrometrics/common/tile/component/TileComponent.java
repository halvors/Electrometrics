package org.halvors.electrometrics.common.tile.component;

import org.halvors.electrometrics.common.tile.TileEntityComponentContainer;

public class TileComponent {
    final TileEntityComponentContainer tileEntity;

    public TileComponent(TileEntityComponentContainer tileEntity) {
        this.tileEntity = tileEntity;
    }

    public TileEntityComponentContainer getTileEntity() {
        return tileEntity;
    }
}
