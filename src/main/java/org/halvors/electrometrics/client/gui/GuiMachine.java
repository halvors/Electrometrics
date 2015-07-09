package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.common.tile.TileEntityElectricBlock;

@SideOnly(Side.CLIENT)
public class GuiMachine extends GuiScreen {
	protected GuiMachine(TileEntityElectricBlock tileEntity) {
		super(tileEntity);
	}

	@Override
	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(tileEntity.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
	}
}