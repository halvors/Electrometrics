package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.util.Location;

import java.util.ArrayList;

/**
 * This is a packet that provides common information for all TileEntity packets, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketTileEntity extends PacketLocation implements IMessage, IMessageHandler<PacketTileEntity, IMessage> {
    private ArrayList dataList;
    private ByteBuf storedBuffer = null;

    public PacketTileEntity() {

    }

    public PacketTileEntity(Location location, ArrayList dataList) {
        super(location);

        this.dataList = dataList;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        storedBuffer = dataStream.copy();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        encode(new Object[] { dataList }, dataStream);
    }

    @Override
    public IMessage onMessage(PacketTileEntity message, MessageContext context) {
        if (message.getLocation().getWorld().isRemote) {
            System.out.println("PacketTileEntity: Client");
        } else {
            System.out.println("PacketTileEntity: Server");
        }

        TileEntity tileEntity = message.getLocation().getTileEntity();

        if (tileEntity != null && tileEntity instanceof ITileEntityNetwork) {
            ITileEntityNetwork tileEntityNetwork = (ITileEntityNetwork) tileEntity;

            try {
                tileEntityNetwork.handlePacketData(message.storedBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            message.storedBuffer.release();
        }

        return null;
    }

    /**
     * Encodes an Object[] of data into a DataOutputStream.
     *
     * @param dataList
     * @param dataStream
     */
    public static void encode(Object[] dataList, ByteBuf dataStream) {
        try {
            for (Object data : dataList) {
                // Language types.
                if (data instanceof Integer) {
                    dataStream.writeInt((Integer) data);
                } else if (data instanceof Double) {
                    dataStream.writeDouble((Double) data);
                } else if (data instanceof Float) {
                    dataStream.writeFloat((Float) data);
                } else if (data instanceof Boolean) {
                    dataStream.writeBoolean((Boolean) data);
                } else if (data instanceof Byte) {
                    dataStream.writeByte((Byte) data);
                } else if (data instanceof String) {
                    ByteBufUtils.writeUTF8String(dataStream, (String) data);

                // Minecraft types.
                } else if (data instanceof ItemStack) {
                    ByteBufUtils.writeItemStack(dataStream, (ItemStack) data);
                } else if (data instanceof NBTTagCompound) {
                    ByteBufUtils.writeTag(dataStream, (NBTTagCompound) data);

                // Array types.
                } else if (data instanceof int[]) {
                    for (int i : (int[]) data) {
                        dataStream.writeInt(i);
                    }
                } else if (data instanceof byte[]) {
                    for (byte b : (byte[]) data) {
                        dataStream.writeByte(b);
                    }

                // Umm. Check this? From what i can understand this is to...
                } else if (data instanceof ArrayList) {
                    encode(((ArrayList) data).toArray(), dataStream);
                }
            }
        } catch (Exception e) {
            Electrometrics.getLogger().error("An error occurred when sending packet data.");
            e.printStackTrace();
        }
    }
}