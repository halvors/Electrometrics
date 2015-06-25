package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PacketTileEntity extends Packet implements IMessage {
    protected World world;
    protected int x;
    protected int y;
    protected int z;

    public PacketTileEntity() {

    }

    public PacketTileEntity(PacketType packetType, World world, int x, int y, int z) {
        super(packetType);

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        world = DimensionManager.getProvider(dataStream.readInt()).worldObj;
        x = dataStream.readInt();
        y = dataStream.readInt();
        z = dataStream.readInt();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(world.provider.dimensionId);
        dataStream.writeInt(x);
        dataStream.writeInt(y);
        dataStream.writeInt(z);
    }
}