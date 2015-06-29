package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.common.util.render.Rectangle4i;

@SideOnly(Side.CLIENT)
public interface IGuiComponent {
    Rectangle4i getBounds(int guiWidth, int guiHeight);

    void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight);

    void renderForeground(int xAxis, int yAxis);

    void preMouseClicked(int xAxis, int yAxis, int button);

    void mouseClicked(int xAxis, int yAxis, int button);

    void mouseClickMove(int mouseX, int mouseY, int button, long ticks);

    void mouseMovedOrUp(int x, int y, int type);
}