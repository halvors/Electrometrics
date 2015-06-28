package org.halvors.electrometrics.common.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * Internal interface used for TileEntities that sends or receives data.
 *
 * @author halvors
 */
public interface INetworkable {
	/**
	 * Receive and manage a packet's data.
	 * @param dataStream
	 */
	void handlePacketData(ByteBuf dataStream) throws Exception;

	/**
	 * Gets an ArrayList of data this tile entity keeps synchronized with the client.
	 * @param data - list of data
	 * @return ArrayList<Object>
	 */
	ArrayList<Object> getPacketData(ArrayList<Object> data);
}

