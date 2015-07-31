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
		General.enableUpdateNotice = dataStream.readBoolean();
        General.destroyDisabledBlocks = dataStream.readBoolean();

        General.toJoules = dataStream.readDouble();
        General.toMinecraftJoules = dataStream.readDouble();
        General.toElectricalUnits = dataStream.readDouble();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, dataStream.readBoolean());
        }

        // Integration.
		Integration.isBuildCraftEnabled = dataStream.readBoolean();
		Integration.isCoFHCoreEnabled = dataStream.readBoolean();
        Integration.isMekanismEnabled = dataStream.readBoolean();

		// TODO: Should we sync this?
		// Client.
		//Client.energyUnit = EnergyUnit.values()[dataStream.readInt()];
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		List<Object> objects = new ArrayList<>();

		// General.
		objects.add(General.enableUpdateNotice);
		objects.add(General.destroyDisabledBlocks);

		objects.add(General.toJoules);
		objects.add(General.toMinecraftJoules);
		objects.add(General.toElectricalUnits);

        // Machine.
        for (MachineType machineType : MachineType.values()) {
			objects.add(Machine.isEnabled(machineType));
        }

        // Integration.
		objects.add(Integration.isBuildCraftEnabled);
		objects.add(Integration.isCoFHCoreEnabled);
		objects.add(Integration.isMekanismEnabled);

		// TODO: Should we sync this?
		// Client.
		//objects.add(Client.energyUnit.ordinal());

		NetworkHandler.writeObjects(objects, dataStream);
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(PacketConfiguration message, MessageContext messageContext) {
			return null;
		}
	}
}
