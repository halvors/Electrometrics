package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ElectricityMeterMessage implements IMessage {
    private double energyCount;

    public ElectricityMeterMessage(double energyCount) {
        this.energyCount = energyCount;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        energyCount = dataStream.readDouble();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        dataStream.writeDouble(energyCount);
    }

    public static class Handler implements IMessageHandler<ElectricityMeterMessage, IMessage> {
        @Override
        public IMessage onMessage(ElectricityMeterMessage message, MessageContext messageContext) {
            System.out.println(String.format("Received %s from %s", message.energyCount, messageContext.getServerHandler().playerEntity.getDisplayName()));

            return null;
        }
    }
}