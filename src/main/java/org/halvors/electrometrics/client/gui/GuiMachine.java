package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.common.tile.machine.TileEntityMachine;

@SideOnly(Side.CLIENT)
public class GuiMachine extends GuiComponentContainerScreen {
	GuiMachine(TileEntityMachine tileEntity) {
		super(tileEntity);
	}
}