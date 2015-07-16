package org.halvors.electrometrics.common.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {
    /**
     * Get the translation for the current text.
     *
     * @param text the text that we want to transelate.
     * @return text
     */
	public static String translate(String text) {
		return StatCollector.translateToLocal(text);
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
