package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import org.halvors.electrometrics.common.util.Location;

/**
 * This is a packet that provides common information for all TileEntity packets, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketTileEntity extends Packet implements IMessage {
    protected Location location;

    public PacketTileEntity() {

    }

    public PacketTileEntity(PacketType packetType, Location location) {
        super(packetType);

        this.location = location;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dataStream.readInt());
        location = new Location(world, dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(location.getWorld().provider.dimensionId);
        dataStream.writeInt(location.getX());
        dataStream.writeInt(location.getY());
        dataStream.writeInt(location.getZ());
    }
}