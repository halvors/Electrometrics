package org.halvors.electrometrics.common.tile.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.INetworkable;
import org.halvors.electrometrics.common.component.IComponent;

import java.util.List;

public interface ITileComponent extends IComponent, INetworkable {
    void onUpdate();

    void readFromNBT(NBTTagCompound nbtTagCompound);

    void writeToNBT(NBTTagCompound nbtTagCompound);

    void handlePacketData(ByteBuf dataStream);

    List<Object> getPacketData(List<Object> list);
}