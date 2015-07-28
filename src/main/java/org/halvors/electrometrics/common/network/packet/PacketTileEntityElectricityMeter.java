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

public class PacketTileEntityElectricityMeter extends PacketLocation implements IMessage {
    private TileEntityElectricityMeter tileEntityElectricityMeter;
    private PacketType packetType;
    private double electricityCount;

    public PacketTileEntityElectricityMeter() {

    }

    public PacketTileEntityElectricityMeter(TileEntityElectricityMeter tileEntityElectricityMeter, PacketType packetType) {
        super(tileEntityElectricityMeter);

        this.tileEntityElectricityMeter = tileEntityElectricityMeter;
        this.packetType = packetType;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        packetType = PacketType.values()[dataStream.readInt()];

        switch (packetType) {
            case GET:
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
            case GET:
                objects.add(tileEntityElectricityMeter.getElectricityCount());
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
                    case GET:
                        if (world.isRemote) {
                            tileEntityElectricityMeter.setElectricityCount(message.electricityCount);
                        }
                        break;

                    case RESET:
                        if (!world.isRemote) {
                            tileEntityElectricityMeter.setElectricityCount(0);
                        }
                        break;
                }
            }

            return null;
        }
    }

    public enum PacketType {
        GET,
        RESET
    }
}
