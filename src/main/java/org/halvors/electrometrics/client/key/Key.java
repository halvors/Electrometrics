package org.halvors.electrometrics.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Key {
    public static GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

    public static KeyBinding sneakKey = gameSettings.keyBindSneak;
    public static KeyBinding jumpKey = gameSettings.keyBindJump;
}
