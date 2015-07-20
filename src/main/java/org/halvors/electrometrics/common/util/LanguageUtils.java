package org.halvors.electrometrics.common.util;

import net.minecraft.util.StatCollector;

public class LanguageUtils {
    /**
     * Get the translation for the current text.
     *
     * @param text the text that we want to transelate.
     * @return text
     */
    public static String translate(String text) {
        return StatCollector.translateToLocal(text);
    }
}
