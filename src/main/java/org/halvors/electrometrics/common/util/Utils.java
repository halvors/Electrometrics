package org.halvors.electrometrics.common.util;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import mekanism.api.IMekWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.base.tile.IRedstoneControl;

import java.util.List;
import java.util.UUID;

public class Utils {
	public static String translate(String text) {
		return StatCollector.translateToLocal(text);
	}

	/**
	 * Whether or not a certain TileEntity can function with redstone logic. Illogical to use unless the defined TileEntity implements
	 * IRedstoneControl.
	 * @param redstoneControl - TileEntity to check
	 * @return if the TileEntity can function with redstone logic
	 */
	public static <T extends TileEntity & IRedstoneControl> boolean canFunction(T redstoneControl) {
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

		return false;
	}

	/**
	 * Whether or not the player has a usable wrench for a block at the coordinates given.
	 * @param player - the player using the wrench
	 * @param x - the x coordinate of the block being wrenched
	 * @param y - the y coordinate of the block being wrenched
	 * @param z - the z coordinate of the block being wrenched
	 * @return if the player can use the wrench
	 */
	public static boolean hasUsableWrench(EntityPlayer player, int x, int y, int z) {
		ItemStack itemStack = player.getCurrentEquippedItem();
		Item item = itemStack.getItem();

		// Check if item is a Buildcraft wrench.
		if (item instanceof IToolWrench) {
			IToolWrench wrench = (IToolWrench) item;

			return wrench.canWrench(player, x, y, z);
		}

		// Check if item is a CoFH wrench.
		if (item instanceof IToolHammer) {
			IToolHammer wrench = (IToolHammer) item;

			return wrench.isUsable(itemStack, player, x, y, z);
		}

		// Check if item is a Mekanism wrench.
		if (item instanceof IMekWrench) {
			IMekWrench wrench = (IMekWrench) item;

			return wrench.canUseWrench(player, x, y, z);
		}

		return false;
	}

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
	 * Get a player from it's unique id.
	 * @param uuid the uuid of the player.
	 * @return the EntityPlayerMP object.
	 */
	@SuppressWarnings("unchecked")
	public static EntityPlayer getPlayerFromUUID(UUID uuid) {
		MinecraftServer server = MinecraftServer.getServer();

		if (server != null) {
			List<EntityPlayer> playerList = (List<EntityPlayer>) server.getConfigurationManager().playerEntityList;

			for (EntityPlayer player : playerList)  {
				if (uuid.equals(player.getPersistentID())) {
					return player;
				}
			}
		}

		return null;
	}
}
