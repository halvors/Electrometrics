package org.halvors.electrometrics.client.gui;

import org.halvors.electrometrics.common.tileentity.TileEntityMachine;

public class GuiMachine extends GuiScreen {
	protected GuiMachine(TileEntityMachine tileEntity) {
		super(tileEntity);
	}

	@Override
	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(tileEntity.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
	}
}