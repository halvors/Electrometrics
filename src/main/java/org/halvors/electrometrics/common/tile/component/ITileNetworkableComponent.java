package org.halvors.electrometrics.common.tile.component;

import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.base.tile.INetworkable;

import java.util.List;

public interface ITileNetworkableComponent extends ITileComponent, INetworkable {
    void handlePacketData(ByteBuf dataStream);

    List<Object> getPacketData(List<Object> list);
}
