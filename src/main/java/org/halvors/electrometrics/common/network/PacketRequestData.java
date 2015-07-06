package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.base.tile.INetworkable;
import org.halvors.electrometrics.common.util.location.BlockLocation;

import java.util.ArrayList;

public class PacketRequestData extends PacketBlockLocation implements IMessage, IMessageHandler<PacketRequestData, IMessage> {
	public PacketRequestData() {

	}

	public PacketRequestData(BlockLocation blockLocation) {
		super(blockLocation);
	}

	public PacketRequestData(INetworkable networkable) {
		super(new BlockLocation((TileEntity) networkable));
	}

	@Override
	public IMessage onMessage(PacketRequestData message, MessageContext context) {
		TileEntity tileEntity = message.getBlockLocation().getTileEntity(PacketHandler.getWorld(context));

		if (tileEntity != null) {
			if (tileEntity instanceof INetworkable) {
				INetworkable networkable = (INetworkable) tileEntity;

				return new PacketTileEntity(message.getBlockLocation(), networkable.getPacketData(new ArrayList<>()));
			}
		}

		return null;
	}
}
