package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.PacketHandler;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

public class ElectricityMeterMessage implements IMessage {
    private int x;
    private int y;
    private int z;
    private double electricityCount;

    public ElectricityMeterMessage() {

    }

    public ElectricityMeterMessage(TileEntityElectricityMeter tileEntity) {
        this.x = tileEntity.xCoord;
        this.y = tileEntity.yCoord;
        this.z = tileEntity.zCoord;
        this.electricityCount = tileEntity.getElectricityCount();
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        x = dataStream.readInt();
        y = dataStream.readInt();
        z = dataStream.readInt();
        electricityCount = dataStream.readDouble();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        dataStream.writeInt(x);
        dataStream.writeInt(y);
        dataStream.writeInt(z);
        dataStream.writeDouble(electricityCount);
    }

    public static class Handler implements IMessageHandler<ElectricityMeterMessage, IMessage> {
        @Override
        public IMessage onMessage(ElectricityMeterMessage message, MessageContext messageContext) {
            EntityPlayer player = PacketHandler.getPlayer(messageContext);
            TileEntity tileEntity = player.worldObj.getTileEntity(message.x, message.y, message.z);

            if (tileEntity instanceof TileEntityElectricityMeter) {
                TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

                tileEntityElectricityMeter.setElectricityCount(message.electricityCount);
            }

            System.out.println(String.format("Received %s from %s", message.electricityCount, PacketHandler.getPlayer(messageContext).getDisplayName()));

            return null;
        }
    }
}