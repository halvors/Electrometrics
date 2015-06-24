package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

public class PacketTileEntityElectricityMeter extends PacketTileEntity implements IMessage, IMessageHandler<PacketTileEntityElectricityMeter, IMessage> {
    private double electricityCount = 0;

    public PacketTileEntityElectricityMeter() {

    }

    public PacketTileEntityElectricityMeter(int x, int y, int z) {
        super(x, y, z);
    }

    public PacketTileEntityElectricityMeter(int x, int y, int z, double electricityCount) {
        super(x, y, z);

        this.electricityCount = electricityCount;
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeDouble(electricityCount);
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        electricityCount = dataStream.readDouble();
    }

    @Override
    public IMessage onMessage(PacketTileEntityElectricityMeter message, MessageContext messageContext) {
        EntityPlayer player = PacketHandler.getPlayer(messageContext);
        TileEntity tileEntity = player.worldObj.getTileEntity(message.x, message.y, message.z);

        if (tileEntity instanceof TileEntityElectricityMeter) {
            TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

            if (messageContext.side.isClient()) {
                tileEntityElectricityMeter.setElectricityCount(message.electricityCount);
            } else {
                // Send the data back to the client.
                return new PacketTileEntityElectricityMeter(tileEntityElectricityMeter.xCoord, tileEntityElectricityMeter.yCoord, tileEntityElectricityMeter.zCoord, tileEntityElectricityMeter.getElectricityCount());
            }
        }

        return null;
    }
}