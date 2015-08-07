package org.halvors.electrometrics.common.tile.component;

import org.halvors.electrometrics.common.component.IComponentContainer;
import org.halvors.electrometrics.common.tile.TileEntity;

public abstract class TileComponentBase<T extends TileEntity & IComponentContainer> implements ITileComponent {
    protected final IComponentContainer componentContainer;
    protected final TileEntity tileEntity;

    public TileComponentBase(T componentContainer) {
        this.componentContainer = componentContainer;
        this.tileEntity = componentContainer;
    }

    public IComponentContainer getComponentContainer() {
        return componentContainer;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }
}
