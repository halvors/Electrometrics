package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.electrometrics.common.ConfigurationManager.General;
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

        General.energyUnitType = EnergyUnit.values()[dataStream.readInt()];
        General.toJoules = dataStream.readDouble();
        General.toMinecraftJoules = dataStream.readDouble();
        General.toElectricalUnits = dataStream.readDouble();

        General.isMekanismIntegrationEnabled = dataStream.readBoolean();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, dataStream.readBoolean());
        }
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		// General.
        dataStream.writeBoolean(General.destroyDisabledBlocks);

		dataStream.writeInt(General.energyUnitType.ordinal());
		dataStream.writeDouble(General.toJoules);
		dataStream.writeDouble(General.toMinecraftJoules);
		dataStream.writeDouble(General.toElectricalUnits);

		dataStream.writeBoolean(General.isMekanismIntegrationEnabled);

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            dataStream.writeBoolean(Machine.isEnabled(machineType));
        }
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(PacketConfiguration message, MessageContext messageContext) {
			return null;
		}
	}
}
