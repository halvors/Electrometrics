package org.halvors.electrometrics.common.tile.component;

import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.component.IComponent;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.TileEntityComponentContainer;

public interface ITileComponent extends IComponent {
    TileEntity getTileEntity();

    void onUpdate();

    void onNeighborChange();

    void readFromNBT(NBTTagCompound nbtTagCompound);

    void writeToNBT(NBTTagCompound nbtTagCompound);
}