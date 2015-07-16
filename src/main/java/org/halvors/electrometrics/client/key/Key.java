package org.halvors.electrometrics.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public enum Key {
    SNEAK(Minecraft.getMinecraft().gameSettings.keyBindSneak),
    JUMP(Minecraft.getMinecraft().gameSettings.keyBindJump);

    private KeyBinding keyBinding;

    Key(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
}
