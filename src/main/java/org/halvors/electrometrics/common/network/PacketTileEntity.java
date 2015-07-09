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
import org.halvors.electrometrics.common.base.tile.INetworkable;
import org.halvors.electrometrics.common.util.location.BlockLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a packet that provides common information for all TileEntity packets, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketTileEntity extends PacketBlockLocation implements IMessage, IMessageHandler<PacketTileEntity, IMessage> {
	private List<Object> objectList;
	private ByteBuf storedBuffer = null;

	public PacketTileEntity() {

	}

	public PacketTileEntity(BlockLocation blockLocation, List<Object> dataList) {
		super(blockLocation);

		this.objectList = dataList;
	}

	public PacketTileEntity(INetworkable networkable) {
		this(new BlockLocation((TileEntity) networkable), networkable.getPacketData(new ArrayList<>()));
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		super.fromBytes(dataStream);

		storedBuffer = dataStream.copy();
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		super.toBytes(dataStream);

		try {
			for (Object object : objectList) {
				// Language types.
				if (object instanceof Integer) {
					dataStream.writeInt((Integer) object);
				} else if (object instanceof Double) {
					dataStream.writeDouble((Double) object);
				} else if (object instanceof Float) {
					dataStream.writeFloat((Float) object);
				} else if (object instanceof Boolean) {
					dataStream.writeBoolean((Boolean) object);
				} else if (object instanceof Byte) {
					dataStream.writeByte((Byte) object);
				} else if (object instanceof String) {
					ByteBufUtils.writeUTF8String(dataStream, (String) object);
				} else if (object instanceof int[]) { // Array types.
					for (int i : (int[]) object) {
						dataStream.writeInt(i);
					}
				} else if (object instanceof byte[]) {
					for (byte b : (byte[]) object) {
						dataStream.writeByte(b);
					}
				} else if (object instanceof ItemStack) { // Minecraft specific types.
					ByteBufUtils.writeItemStack(dataStream, (ItemStack) object);
				} else if (object instanceof NBTTagCompound) {
					ByteBufUtils.writeTag(dataStream, (NBTTagCompound) object);
				}
			}
		} catch (Exception e) {
			Electrometrics.getLogger().error("An error occurred when sending packet data.");
			e.printStackTrace();
		}
	}

	@Override
	public IMessage onMessage(PacketTileEntity message, MessageContext context) {
		TileEntity tileEntity = message.getBlockLocation().getTileEntity(PacketHandler.getWorld(context));

		if (tileEntity instanceof INetworkable) {
			INetworkable networkable = (INetworkable) tileEntity;

			try {
				networkable.handlePacketData(message.storedBuffer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			message.storedBuffer.release();
		}

		return null;
	}
}