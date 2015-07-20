package org.halvors.electrometrics.common.base;

import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.common.Reference;

public enum ResourceType {
    GUI("gui"),
    GUI_ELEMENT("gui/elements"),
    SOUND("sound"),
    TEXTURE_BLOCKS("textures/blocks"),
    TEXTURE_ITEMS("textures/items");

    private final String prefix;

    ResourceType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix + "/";
    }
}