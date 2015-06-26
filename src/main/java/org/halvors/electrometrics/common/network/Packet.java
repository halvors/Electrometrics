package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * This is a basic packet that implements packet types, and is meant to be extended.
 *
 * @author halvors
 */
public class Packet implements IMessage {
    protected PacketType packetType;

    public Packet() {

    }

    public Packet(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        packetType = PacketType.values()[dataStream.readInt()];
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        dataStream.writeInt(packetType.ordinal());
    }
}
