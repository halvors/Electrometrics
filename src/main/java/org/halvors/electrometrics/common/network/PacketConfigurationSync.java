package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.UnitDisplay;

public class PacketConfigurationSync implements IMessage, IMessageHandler<IMessage, IMessage> {
    public PacketConfigurationSync() {

    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        Electrometrics.energyType = UnitDisplay.Unit.values()[dataStream.readInt()];
        Electrometrics.toJoules = dataStream.readDouble();
        Electrometrics.toMinecraftJoules = dataStream.readDouble();
        Electrometrics.toElectricalUnits = dataStream.readDouble();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        dataStream.writeInt(Electrometrics.energyType.ordinal());
        dataStream.writeDouble(Electrometrics.toJoules);
        dataStream.writeDouble(Electrometrics.toMinecraftJoules);
        dataStream.writeDouble(Electrometrics.toElectricalUnits);
    }

    @Override
    public IMessage onMessage(IMessage message, MessageContext messageContext) {
        return null;
    }
}
