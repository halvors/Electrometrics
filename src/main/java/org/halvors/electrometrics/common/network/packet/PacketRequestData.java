package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.component.ITileComponent;

public class PacketRequestData<TE extends TileEntity & ITileNetworkable, TC extends ITileComponent & ITileNetworkable> extends PacketLocation implements IMessage {
	public PacketRequestData() {

	}

	public PacketRequestData(TE tileEntity) {
		super(tileEntity);
	}

	public PacketRequestData(TC tileComponent) {
		this((TE) tileComponent.getTileEntity());
	}

	public static class PacketRequestDataMessage implements IMessageHandler<PacketRequestData, IMessage>{
		@Override
		public IMessage onMessage(PacketRequestData message, MessageContext messageContext) {
			return onPacketRequestDataMessage(message, messageContext);
		}

		@SuppressWarnings("unchecked")
		public IMessage onPacketRequestDataMessage(PacketRequestData message, MessageContext messageContext) {
			TileEntity tileEntity = message.getLocation().getTileEntity(NetworkHandler.getWorld(messageContext));

			if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
				return new PacketTileEntity(tileEntity);
			}

			return null;
		}
	}
}
