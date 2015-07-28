package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;

public class PacketTileEntityElectricityMeter extends PacketLocation implements IMessage {
    public PacketTileEntityElectricityMeter() {

    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);
    }

    public static class PacketTileEntityElectricityMeterMessage implements IMessageHandler<PacketTileEntityElectricityMeter, IMessage> {
        @Override
        public IMessage onMessage(PacketTileEntityElectricityMeter message, MessageContext messageContext) {
            TileEntity tileEntity = message.getLocation().getTileEntity(NetworkHandler.getWorld(messageContext));

            if (tileEntity != null && tileEntity instanceof TileEntityElectricityMeter) {

            }

            return null;
        }
    }
}
