package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.util.Location;

import java.util.ArrayList;

public class PacketRequestData extends PacketLocation implements IMessage, IMessageHandler<PacketRequestData, IMessage> {
    public PacketRequestData() {

    }

    public PacketRequestData(Location location) {
        super(location);
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);
    }

    @Override
    public IMessage onMessage(PacketRequestData message, MessageContext context) {
        if (context.side.isClient()) {
            System.out.println("PacketRequestData: Client");
        } else {
            System.out.println("PacketRequestData: Server");
        }

        TileEntity tileEntity = message.getLocation().getTileEntity();

        if (tileEntity != null && tileEntity instanceof ITileEntityNetwork) {
            ITileEntityNetwork tileEntityNetwork = (ITileEntityNetwork) tileEntity;

            return new PacketTileEntity(message.getLocation(), tileEntityNetwork.getPacketData(new ArrayList()));
        }

        return null;
    }
}
