package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
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
}