package org.halvors.electrometrics.common.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * Internal interface used for blocks that send data between clients and the server
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
     * @return ArrayList
     */
    ArrayList<Object> getPacketData(ArrayList data);
}

