package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
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
    public IMessage onMessage(PacketRequestData message, MessageContext context) {
        TileEntity tileEntity = message.getLocation().getTileEntity();

        if (tileEntity instanceof ITileEntityNetwork) {
            ITileEntityNetwork tileEntityNetwork = (ITileEntityNetwork) tileEntity;

            return new PacketTileEntity(message.getLocation(), tileEntityNetwork.getPacketData(new ArrayList()));
        }

        return null;
    }
}
