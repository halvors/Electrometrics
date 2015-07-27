package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.ConfigurationManager.Client;
import org.halvors.electrometrics.common.ConfigurationManager.General;
import org.halvors.electrometrics.common.ConfigurationManager.Integration;
import org.halvors.electrometrics.common.ConfigurationManager.Machine;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.util.energy.EnergyUnit;

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
        General.destroyDisabledBlocks = dataStream.readBoolean();

        General.toJoules = dataStream.readDouble();
        General.toMinecraftJoules = dataStream.readDouble();
        General.toElectricalUnits = dataStream.readDouble();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, dataStream.readBoolean());
        }

        // Integration.
        Integration.isMekanismEnabled = dataStream.readBoolean();

		// Client.
		Client.energyUnitType = EnergyUnit.values()[dataStream.readInt()];
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		// General.
        dataStream.writeBoolean(General.destroyDisabledBlocks);

		dataStream.writeDouble(General.toJoules);
		dataStream.writeDouble(General.toMinecraftJoules);
		dataStream.writeDouble(General.toElectricalUnits);

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            dataStream.writeBoolean(Machine.isEnabled(machineType));
        }

        // Integration.
        dataStream.writeBoolean(Integration.isMekanismEnabled);

		// Client.
		dataStream.writeInt(Client.energyUnitType.ordinal());
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(PacketConfiguration message, MessageContext messageContext) {
			return null;
		}
	}
}
