package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.util.Location;

/**
 * This is a packet that provides a location, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketLocation implements IMessage {
	private Location location;

	public PacketLocation() {

	}

	public PacketLocation(Location location) {
		this.location = location;
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		location = new Location(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		dataStream.writeInt(location.getX());
		dataStream.writeInt(location.getY());
		dataStream.writeInt(location.getZ());
	}

	public Location getLocation() {
		return location;
	}
}