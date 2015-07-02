package org.halvors.electrometrics.common.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Internal interface used for TileEntities that sends or receives data.
 *
 * @author halvors
 */
public interface INetworkable {
	/**
	 * Receive and manage a packet's data.
	 * @param dataStream the dataStream to read data from.
	 */
	void handlePacketData(ByteBuf dataStream) throws Exception;

	/**
	 * Gets an ArrayList of data this tile entity keeps synchronized with the client.
	 * @param list - list of objects
	 * @return List<Object>
	 */
	List<Object> getPacketData(List<Object> list);
}

