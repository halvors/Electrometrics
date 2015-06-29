package org.halvors.electrometrics.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Makes you able to get offline players names from UUID.
 *
 * @author Giraffestock
 */
public class UUIDHelper {
    private static final HashMap<String, String> uuidUsernameLookupCache = new HashMap();

    public static String getUsernameForUUID(UUID uuid, boolean useTheCache) {
        String playerUniqueID = uuid.toString().replaceAll("-", "");

        if (useTheCache && uuidUsernameLookupCache.containsKey(playerUniqueID)) {
            return uuidUsernameLookupCache.get(playerUniqueID);
        }

        try {
            String jsonText = getHtmlText("https://api.mojang.com/user/profiles/" + playerUniqueID + "/names");
            JsonArray jsonNames = new JsonParser().parse(jsonText).getAsJsonArray();

            for (JsonElement jsonNameElem : jsonNames) {
                if (!jsonNameElem.getAsJsonObject().has("changedToAt")) {
                    String name = jsonNameElem.getAsJsonObject().get("name").getAsString();

                    if (useTheCache) {
                        uuidUsernameLookupCache.put(playerUniqueID, name);
                    }

                    return name;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getPlayerUUID(EntityPlayer player){
        return player.getGameProfile().getId().toString();
    }

    public static String getHtmlText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();
    }
}
