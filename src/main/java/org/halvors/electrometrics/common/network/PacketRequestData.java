package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.location.BlockLocation;

public class PacketRequestData extends PacketBlockLocation implements IMessage, IMessageHandler<PacketRequestData, IMessage> {
	public PacketRequestData() {

	}

	public PacketRequestData(BlockLocation blockLocation) {
		super(blockLocation);
	}

	public PacketRequestData(ITileNetworkable networkable) {
		super(new BlockLocation((TileEntity) networkable));
	}

	@Override
	public IMessage onMessage(PacketRequestData message, MessageContext context) {
		TileEntity tileEntity = message.getBlockLocation().getTileEntity(PacketHandler.getWorld(context));

		if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
			return new PacketTileEntity((ITileNetworkable) tileEntity);
		}

		return null;
	}
}
