package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.Location;

/**
 * This is a packet used by the ElectricityMeter to synchronize fields between the server and the client.
 *
 * @author halvors
 */
public class PacketElectricityMeter extends PacketTileEntity implements IMessage, IMessageHandler<PacketElectricityMeter, IMessage> {
    private double electricityCount;

    public PacketElectricityMeter() {

    }

    public PacketElectricityMeter(PacketType packetType, Location location) {
        super(packetType, location);
    }

    public PacketElectricityMeter(PacketType packetType, Location location, double electricityCount) {
        super(packetType, location);

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
        //EntityPlayer player = PacketHandler.getPlayer(messageContext);
        TileEntity tileEntity = message.location.getTileEntity();

        if (tileEntity instanceof TileEntityElectricityMeter) {
            TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

            if (message.packetType == PacketType.GET) {
                // Send a SET packet back to the sender.
                return new PacketElectricityMeter(PacketType.SET, message.location, tileEntityElectricityMeter.getElectricityCount());
            } else if (message.packetType == PacketType.SET) {
                // Update the tileEntity with the value of this SET packet.
                tileEntityElectricityMeter.setElectricityCount(message.electricityCount);
            }
        }

        if (tileEntity == null) {
            System.out.println("Something therible happend, tileEntity is null!!!!");
        }

        return null;
    }
}