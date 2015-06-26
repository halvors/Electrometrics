package org.halvors.electrometrics.common.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * Internal interface used for blocks that send data between clients and the server
 *
 * @author halvors
 */
public interface ITileEntityNetwork {
    /**
     * Receive and manage a packet's data.
     * @param dataStream
     */
    public void handlePacketData(ByteBuf dataStream) throws Exception;

    /**
     * Gets an ArrayList of data this tile entity keeps synchronized with the client.
     * @param data - list of data
     * @return ArrayList
     */
    public ArrayList getPacketData(ArrayList data);
}

