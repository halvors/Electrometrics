package org.halvors.electrometrics.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Key {
    private static final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

    public static final KeyBinding sneakKey = gameSettings.keyBindSneak;
    public static final KeyBinding jumpKey = gameSettings.keyBindJump;
}
