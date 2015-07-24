package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.common.component.IComponent;

@SideOnly(Side.CLIENT)
public interface IGuiComponent extends IComponent {
	void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight, int xSize, int ySize);

	void renderForeground(int xAxis, int yAxis, int xSize, int ySize);

	void preMouseClicked(int xAxis, int yAxis, int xSize, int ySize, int button);

	void mouseClicked(int xAxis, int yAxis, int xSize, int ySize, int button);

	void mouseClickMove(int mouseX, int mouseY, int button, long ticks);

	void mouseMovedOrUp(int x, int y, int type);
}