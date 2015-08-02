package org.halvors.electrometrics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.client.gui.component.GuiEnergyUnitType;
import org.halvors.electrometrics.client.gui.component.GuiRedstoneControl;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricMachine;

@SideOnly(Side.CLIENT)
public class GuiElectricMachine extends GuiMachine {
	protected GuiElectricMachine(TileEntityElectricMachine tileEntity) {
		super(tileEntity);

		components.add(new GuiEnergyUnitType(this, defaultResource));
		components.add(new GuiRedstoneControl<>(this, tileEntity, defaultResource));
	}
}