package org.halvors.electrometrics.common.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.tileentity.IRedstoneControl;

import java.util.List;
import java.util.UUID;

public class Utils {
	/**
	 * Converts the energy to the default energy system.
	 * @param energy the raw energy.
	 * @return the energy as a String.
	 */
	public static String getEnergyDisplay(double energy) {
		switch (Electrometrics.energyType) {
			case REDSTONE_FLUX:
				return UnitDisplay.getDisplayShort(energy, UnitDisplay.Unit.REDSTONE_FLUX);

			case JOULES:
				return UnitDisplay.getDisplayShort(energy * Electrometrics.toJoules, UnitDisplay.Unit.JOULES);

			case MINECRAFT_JOULES:
				return UnitDisplay.getDisplayShort(energy * Electrometrics.toMinecraftJoules, UnitDisplay.Unit.MINECRAFT_JOULES);

			case ELECTRICAL_UNITS:
				return UnitDisplay.getDisplayShort(energy * Electrometrics.toElectricalUnits, UnitDisplay.Unit.MINECRAFT_JOULES);
		}

		return null;
	}

	/**
	 * Whether or not a certain TileEntity can function with redstone logic. Illogical to use unless the defined TileEntity implements
	 * IRedstoneControl.
	 * @param tileEntity - TileEntity to check
	 * @return if the TileEntity can function with redstone logic
	 */
	public static boolean canFunction(TileEntity tileEntity) {
		if (tileEntity instanceof IRedstoneControl) {
			IRedstoneControl redstoneControl = (IRedstoneControl) tileEntity;

			switch (redstoneControl.getControlType()) {
				case DISABLED:
					return true;

				case HIGH:
					return redstoneControl.isPowered();

				case LOW:
					return !redstoneControl.isPowered();

				case PULSE:
					return redstoneControl.isPowered() && !redstoneControl.wasPowered();
			}
		}

		return false;
	}

	/**
	 * Get a player from it's unique id.
	 * @param uuid the uuid of the player.
	 * @return the EntityPlayerMP object.
	 */
	public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {
		MinecraftServer server = MinecraftServer.getServer();

		if (server != null) {
			List<EntityPlayerMP> playerList = (List<EntityPlayerMP>) server.getConfigurationManager().playerEntityList;

			for (EntityPlayerMP player : playerList)  {
				if (uuid.equals(player.getPersistentID())) {
					return player;
				}
			}
		}

		return null;
	}
}
