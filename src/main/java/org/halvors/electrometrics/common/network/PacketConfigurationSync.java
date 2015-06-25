package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.UnitDisplay.Unit;

public class PacketConfigurationSync extends Packet implements IMessage, IMessageHandler<PacketConfigurationSync, IMessage> {
    public PacketConfigurationSync() {

    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        Electrometrics.energyType = Unit.values()[dataStream.readInt()];
        Electrometrics.toJoules = dataStream.readDouble();
        Electrometrics.toMinecraftJoules = dataStream.readDouble();
        Electrometrics.toElectricalUnits = dataStream.readDouble();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(Electrometrics.energyType.ordinal());
        dataStream.writeDouble(Electrometrics.toJoules);
        dataStream.writeDouble(Electrometrics.toMinecraftJoules);
        dataStream.writeDouble(Electrometrics.toElectricalUnits);
    }

    @Override
    public IMessage onMessage(PacketConfigurationSync message, MessageContext messageContext) {
        return null;
    }
}
