package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.util.energy.Unit;

/**
 * This is a packet that synchronizes the configuration from the server to the clients.
 *
 * @author halvors
 */
public class PacketConfiguration implements IMessage {
	public PacketConfiguration() {

	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
        // General.
        Electrometrics.energyUnitType = Unit.values()[dataStream.readInt()];
		Electrometrics.toJoules = dataStream.readDouble();
		Electrometrics.toMinecraftJoules = dataStream.readDouble();
		Electrometrics.toElectricalUnits = dataStream.readDouble();

        // Mod integration.
        Electrometrics.isMekanismIntegrationEnabled = dataStream.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
        // General.
        dataStream.writeInt(Electrometrics.energyUnitType.ordinal());
		dataStream.writeDouble(Electrometrics.toJoules);
		dataStream.writeDouble(Electrometrics.toMinecraftJoules);
		dataStream.writeDouble(Electrometrics.toElectricalUnits);

        // Mod integration.
        dataStream.writeBoolean(Electrometrics.isMekanismIntegrationEnabled);
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(PacketConfiguration message, MessageContext messageContext) {
			return null;
		}
	}
}
