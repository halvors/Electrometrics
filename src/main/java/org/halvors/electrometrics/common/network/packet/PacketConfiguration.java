package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.ConfigurationManager.General;
import org.halvors.electrometrics.common.ConfigurationManager.Integration;
import org.halvors.electrometrics.common.ConfigurationManager.Machine;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;

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
        General.toElectricalUnits = dataStream.readDouble();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, dataStream.readBoolean());
        }

        // Integration.
		Integration.isBuildCraftEnabled = dataStream.readBoolean();
		Integration.isCoFHCoreEnabled = dataStream.readBoolean();
        Integration.isMekanismEnabled = dataStream.readBoolean();

		// Client.
		// We don't sync this as this is client specific changes that the server shouldn't care about.
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		List<Object> objects = new ArrayList<>();

		// General.
		objects.add(General.destroyDisabledBlocks);

		objects.add(General.toJoules);
		objects.add(General.toElectricalUnits);

        // Machine.
        for (MachineType machineType : MachineType.values()) {
			objects.add(Machine.isEnabled(machineType));
        }

        // Integration.
		objects.add(Integration.isBuildCraftEnabled);
		objects.add(Integration.isCoFHCoreEnabled);
		objects.add(Integration.isMekanismEnabled);

		// Client.
		// We don't sync this as this is client specific changes that the server shouldn't care about.

		NetworkHandler.writeObjects(objects, dataStream);
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(PacketConfiguration message, MessageContext messageContext) {
			return null;
		}
	}
}
