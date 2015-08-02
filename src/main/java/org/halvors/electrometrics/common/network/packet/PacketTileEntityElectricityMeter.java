package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a packet that provides common information for all TileEntity packets, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketTileEntityElectricityMeter extends PacketLocation implements IMessage {
    private PacketType packetType;
    private double electricityCount;

    public PacketTileEntityElectricityMeter() {

    }

    public PacketTileEntityElectricityMeter(TileEntityElectricityMeter tileEntity, PacketType packetType) {
        super(tileEntity);

        this.packetType = packetType;

        switch (packetType) {
            case RESPONSE:
                electricityCount = tileEntity.getElectricityCount();
                break;
        }
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        packetType = PacketType.values()[dataStream.readInt()];

        switch (packetType) {
            case RESPONSE:
                electricityCount = dataStream.readDouble();
                break;
        }
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        List<Object> objects = new ArrayList<>();
        objects.add(packetType.ordinal());

        switch (packetType) {
            case RESPONSE:
                objects.add(electricityCount);
                break;
        }

        NetworkHandler.writeObjects(objects, dataStream);
    }

    public double getElectricityCount() {
        return electricityCount;
    }

    public static class PacketTileEntityElectricityMeterMessage implements IMessageHandler<PacketTileEntityElectricityMeter, IMessage> {
        @Override
        public IMessage onMessage(PacketTileEntityElectricityMeter message, MessageContext messageContext) {
            World world = NetworkHandler.getWorld(messageContext);
            TileEntity tileEntity = message.getLocation().getTileEntity(world);

            if (tileEntity != null && tileEntity instanceof TileEntityElectricityMeter) {
                TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

                switch (message.packetType) {
                    case REQUEST:
                        if (messageContext.side.isServer()) {
                            return new PacketTileEntityElectricityMeter(tileEntityElectricityMeter, PacketType.RESPONSE);
                        }
                        break;

                    case RESPONSE:
                        if (messageContext.side.isClient()) {
                            tileEntityElectricityMeter.setElectricityCount(message.electricityCount);
                        }
                        break;

                    case RESET:
                        if (messageContext.side.isServer()) {
                            tileEntityElectricityMeter.setElectricityCount(0);

                            return new PacketTileEntityElectricityMeter(tileEntityElectricityMeter, PacketType.RESPONSE);
                        }
                        break;
                }
            }

            return null;
        }
    }

    public enum PacketType {
        REQUEST,
        RESPONSE,
        RESET
    }
}