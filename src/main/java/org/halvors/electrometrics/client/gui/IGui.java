package org.halvors.electrometrics.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IIcon;

import java.util.List;

public interface IGui {
    void drawTexturedRect(int x, int y, int u, int v, int w, int h);

    void drawTexturedRectFromIcon(int x, int y, IIcon icon, int w, int h);

    void displayTooltip(String s, int xAxis, int yAxis);

    void displayTooltips(List<String> list, int xAxis, int yAxis);

    FontRenderer getFontRenderer();
}

