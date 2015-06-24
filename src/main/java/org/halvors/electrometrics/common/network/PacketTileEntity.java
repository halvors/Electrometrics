package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketTileEntity implements IMessage {
    public int x;
    public int y;
    public int z;

    public PacketTileEntity() {

    }

    public PacketTileEntity(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        dataStream.writeInt(x);
        dataStream.writeInt(y);
        dataStream.writeInt(z);
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        x = dataStream.readInt();
        y = dataStream.readInt();
        z = dataStream.readInt();
    }
}