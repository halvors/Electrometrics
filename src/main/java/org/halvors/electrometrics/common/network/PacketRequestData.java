package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.component.ITileComponent;
import org.halvors.electrometrics.common.util.location.BlockLocation;

public class PacketRequestData extends PacketBlockLocation implements IMessage {
	public PacketRequestData() {

    }

	public PacketRequestData(BlockLocation blockLocation) {
		super(blockLocation);
	}

	public PacketRequestData(TileEntity tileEntity) {
		this(new BlockLocation(tileEntity));
	}

	public PacketRequestData(ITileComponent tileComponent) {
		this(tileComponent.getTileEntity());
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {

	}

	@Override
	public void toBytes(ByteBuf dataStream) {

	}

	public static class PacketRequestDataMessage implements IMessageHandler<PacketRequestData, IMessage> {
		@Override
		public IMessage onMessage(PacketRequestData message, MessageContext messageContext) {
			return onPacketRequestDataMessage(message, messageContext);
		}

        public <T extends TileEntity & ITileNetworkable> IMessage onPacketRequestDataMessage(PacketRequestData message, MessageContext messageContext) {
            TileEntity tileEntity = message.getBlockLocation().getTileEntity(PacketHandler.getWorld(messageContext));

            if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
                return new PacketTileEntity((T) tileEntity);
            }

            return null;
        }
	}
}
