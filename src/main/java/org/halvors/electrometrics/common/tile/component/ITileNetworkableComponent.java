package org.halvors.electrometrics.common.tile.component;

import io.netty.buffer.ByteBuf;

import java.util.List;

public interface ITileNetworkableComponent extends ITileComponent {
    void handlePacketData(ByteBuf dataStream);

    List<Object> getPacketData(List<Object> list);
}
