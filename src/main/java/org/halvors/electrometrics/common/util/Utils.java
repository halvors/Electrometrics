package org.halvors.electrometrics.common.util;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import mekanism.api.IMekWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.base.tile.IRedstoneControl;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.UnitDisplay.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {
	public static String translate(String text) {
		return StatCollector.translateToLocal(text);
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
     * Converts the energy to the default energy system.
     * @param energy the raw energy.
     * @return the energy as a String.
     */
    public static String getEnergyDisplay(double energy) {
        Unit energyType = Electrometrics.energyType;
        double multiplier = 0;

        switch (energyType) {
            case JOULES:
                multiplier = Electrometrics.toJoules;
                break;

            case MINECRAFT_JOULES:
                multiplier = Electrometrics.toMinecraftJoules;
                break;

            case ELECTRICAL_UNITS:
                multiplier = Electrometrics.toElectricalUnits;
                break;
        }

        return UnitDisplay.getDisplayShort(energy * multiplier, energyType);
    }

	/**
	 * Get a player from it's unique id.
	 * @param uuid the uuid of the player.
	 * @return the EntityPlayerMP object.
	 */
	public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {
        for (EntityPlayerMP player : getPlayers()) {
            if (uuid.equals(player.getPersistentID())) {
                return player;
            }
        }

		return null;
	}

    @SuppressWarnings("unchecked")
    public static List<EntityPlayerMP> getPlayers() {
        List<EntityPlayerMP> playerList = new ArrayList<>();
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null) {
            return (List<EntityPlayerMP>) server.getConfigurationManager().playerEntityList;
        }

        return playerList;
    }
}
