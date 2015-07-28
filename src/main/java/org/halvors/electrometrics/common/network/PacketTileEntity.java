package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a packet that provides common information for all TileEntity packets, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketTileEntity extends PacketLocation implements IMessage {
	private List<Object> objects;
	private ByteBuf storedBuffer = null;

	public PacketTileEntity() {

	}

	public PacketTileEntity(Location location, List<Object> objects) {
		super(location);

		this.objects = objects;
	}

	public <T extends TileEntity & ITileNetworkable> PacketTileEntity(T tile) {
		this(new Location(tile), tile.getPacketData(new ArrayList<>()));
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
			for (Object object : objects) {
				// Language types.
				if (object instanceof Boolean) {
					dataStream.writeBoolean((Boolean) object);
				} else if (object instanceof Byte) {
					dataStream.writeByte((Byte) object);
				} else if (object instanceof byte[]) {
					dataStream.writeBytes((byte[]) object);
				} else if (object instanceof Double) {
					dataStream.writeDouble((Double) object);
				} else if (object instanceof Float) {
					dataStream.writeFloat((Float) object);
				} else if (object instanceof Integer) {
					dataStream.writeInt((Integer) object);
				} else if (object instanceof int[]) {
					for (int i : (int[]) object) {
						dataStream.writeInt(i);
					}
				} else if (object instanceof Long) {
					dataStream.writeLong((Long) object);
				} else if (object instanceof String) {
					ByteBufUtils.writeUTF8String(dataStream, (String) object);
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

	public static class PacketTileEntityMessage implements IMessageHandler<PacketTileEntity, IMessage> {
		@Override
		public IMessage onMessage(PacketTileEntity message, MessageContext messageContext) {
			TileEntity tileEntity = message.getLocation().getTileEntity(PacketHandler.getWorld(messageContext));

			if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
				ITileNetworkable tileNetworkable = (ITileNetworkable) tileEntity;

				try {
					tileNetworkable.handlePacketData(message.storedBuffer);
				} catch (Exception e) {
					e.printStackTrace();
				}

				message.storedBuffer.release();
			}

			return null;
		}
	}
}