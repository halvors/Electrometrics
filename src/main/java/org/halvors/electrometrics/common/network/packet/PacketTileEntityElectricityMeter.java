package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;

import java.util.ArrayList;
import java.util.List;

public class PacketTileEntityElectricityMeter extends PacketLocation implements IMessage {
    private PacketType packetType;
    private TileEntityElectricityMeter tileEntityElectricityMeter;

    public PacketTileEntityElectricityMeter() {

    }

    public PacketTileEntityElectricityMeter(PacketType packetType, TileEntityElectricityMeter tileEntityElectricityMeter) {
        this.packetType = packetType;
        this.tileEntityElectricityMeter = tileEntityElectricityMeter;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        packetType = PacketType.values()[dataStream.readInt()];

        switch (packetType) {
            case GET:
                tileEntityElectricityMeter.setElectricityCount(dataStream.readDouble());
                break;

            case SET:
                break;
        }
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        List<Object> objects = new ArrayList<>();
        objects.add(packetType.ordinal());

        switch (packetType) {
            case GET:
                objects.add(tileEntityElectricityMeter.getElectricityCount());
                break;

            case SET:
                break;
        }

        NetworkHandler.writeObjects(objects, dataStream);
    }

    public static class PacketTileEntityElectricityMeterMessage implements IMessageHandler<PacketTileEntityElectricityMeter, IMessage> {
        @Override
        public IMessage onMessage(PacketTileEntityElectricityMeter message, MessageContext messageContext) {
            TileEntity tileEntity = message.getLocation().getTileEntity(NetworkHandler.getWorld(messageContext));

            if (tileEntity != null && tileEntity instanceof TileEntityElectricityMeter) {
                TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

                
            }

            return null;
        }
    }

    public enum PacketType {
        GET,
        SET
    }
}
