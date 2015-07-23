package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.location.BlockLocation;

public class PacketRequestData extends PacketBlockLocation implements IMessage {
	public PacketRequestData() {

	}

	public <T extends TileEntity & ITileNetworkable> PacketRequestData(T tile) {
		super(tile);
	}

	public static class PacketRequestDataMessage implements IMessageHandler<PacketRequestData, IMessage>{
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
