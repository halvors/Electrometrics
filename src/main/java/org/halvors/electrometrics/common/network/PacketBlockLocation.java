package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.location.BlockLocation;

/**
 * This is a packet that provides a blockLocation, and is meant to be extended.
 *
 * @author halvors
 */
class PacketBlockLocation implements IMessage {
	private BlockLocation blockLocation;

	public PacketBlockLocation() {

	}

	PacketBlockLocation(BlockLocation blockLocation) {
		this.blockLocation = blockLocation;
	}

	PacketBlockLocation(Entity entity) {
		this(new BlockLocation(entity));
	}

	PacketBlockLocation(TileEntity tileEntity) {
		this(new BlockLocation(tileEntity));
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		this.blockLocation = new BlockLocation(dataStream.readInt(), dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		dataStream.writeInt(blockLocation.getDimensionId());
		dataStream.writeInt(blockLocation.getX());
		dataStream.writeInt(blockLocation.getY());
		dataStream.writeInt(blockLocation.getZ());
	}

	public BlockLocation getBlockLocation() {
		return blockLocation;
	}

	public static class PacketBlockLocationMessage implements IMessageHandler<PacketBlockLocation, IMessage> {
		@Override
		public IMessage onMessage(PacketBlockLocation message, MessageContext messageContext) {
			return null;
		}
	}
}