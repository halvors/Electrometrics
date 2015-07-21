package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.component.ITileComponent;
import org.halvors.electrometrics.common.util.location.BlockLocation;

public class PacketRequestData<T extends TileEntity & ITileNetworkable> implements IMessageHandler<PacketRequestData.PacketRequestDataMessage, IMessage> {
	@Override
	public IMessage onMessage(PacketRequestDataMessage message, MessageContext context) {
		TileEntity tileEntity = null; //= message.getBlockLocation().getTileEntity(PacketHandler.getWorld(context));

		if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
			return new PacketTileEntity((T) tileEntity);
		}

		return null;
	}

	public static class PacketRequestDataMessage<T extends TileEntity & ITileNetworkable> implements IMessage {
		public PacketRequestDataMessage() {

		}

		public PacketRequestDataMessage(BlockLocation blockLocation) {
			//super(blockLocation);
		}

		public PacketRequestDataMessage(T tileEntity) {
			//super(new BlockLocation(tileEntity));
		}

		public PacketRequestDataMessage(ITileComponent tileComponent) {
			//super(new BlockLocation(tileComponent.getTileEntity()));
		}

		@Override
		public void fromBytes(ByteBuf dataStream) {

		}

		@Override
		public void toBytes(ByteBuf dataStream) {

		}
	}
}
