package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.tileentity.INetworkable;
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
	public void fromBytes(ByteBuf dataStream) {
		super.fromBytes(dataStream);
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		super.toBytes(dataStream);
	}

	@Override
	public IMessage onMessage(PacketRequestData message, MessageContext context) {
		TileEntity tileEntity = message.getBlockLocation().getTileEntity(PacketHandler.getWorld(context));

		if (tileEntity != null && tileEntity instanceof INetworkable) {
			INetworkable networkable = (INetworkable) tileEntity;

			return new PacketTileEntity(message.getBlockLocation(), networkable.getPacketData(new ArrayList<Object>()));
		}

		return null;
	}
}
