package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

public class PacketElectricityMeter extends PacketTileEntity implements IMessage, IMessageHandler<PacketElectricityMeter, IMessage> {
    private double electricityCount;

    public PacketElectricityMeter() {

    }

    public PacketElectricityMeter(PacketType packetType, World world, int x, int y, int z) {
        super(packetType, world, x, y, z);
    }

    public PacketElectricityMeter(PacketType packetType, World world, int x, int y, int z, double electricityCount) {
        super(packetType, world, x, y, z);

        this.electricityCount = electricityCount;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        electricityCount = dataStream.readDouble();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeDouble(electricityCount);
    }

    @Override
    public IMessage onMessage(PacketElectricityMeter message, MessageContext messageContext) {
        TileEntity tileEntity = message.world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileEntityElectricityMeter) {
            TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

            if (message.packetType == PacketType.GET) {
                // Send the data to the sender of the packet.
                return new PacketElectricityMeter(PacketType.GET, message.world, message.x, message.y, message.z, tileEntityElectricityMeter.getElectricityCount());
            } else if (message.packetType == PacketType.SET) {
                tileEntityElectricityMeter.setElectricityCount(message.electricityCount);
            }
        }

        return null;
    }
}