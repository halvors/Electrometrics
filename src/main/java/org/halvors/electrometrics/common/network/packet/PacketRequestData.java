package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.component.ITileComponent;

public class PacketRequestData extends PacketLocation implements IMessage {
	public PacketRequestData() {

	}

	public <T extends TileEntity & ITileNetworkable> PacketRequestData(T tile) {
		super(tile);
	}

	public PacketRequestData(ITileComponent tileComponent) {
		this(tileComponent.getTileEntity());
	}

	public static class PacketRequestDataMessage implements IMessageHandler<PacketRequestData, IMessage>{
		@Override
		public IMessage onMessage(PacketRequestData message, MessageContext messageContext) {
			return onPacketRequestDataMessage(message, messageContext);
		}

		@SuppressWarnings("unchecked")
		public <T extends TileEntity & ITileNetworkable> IMessage onPacketRequestDataMessage(PacketRequestData message, MessageContext messageContext) {
			TileEntity tileEntity = message.getLocation().getTileEntity(NetworkHandler.getWorld(messageContext));

			if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
				return new PacketTileEntity((T) tileEntity);
			}

			return null;
		}
	}
}
