package org.halvors.electrometrics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.client.gui.component.GuiEnergyUnitType;
import org.halvors.electrometrics.client.gui.component.GuiRedstoneControl;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.tile.TileEntityComponentContainer;
import org.halvors.electrometrics.common.tile.component.TileRedstoneControlComponent;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricMachine;

@SideOnly(Side.CLIENT)
public class GuiElectricMachine extends GuiMachine {
	protected GuiElectricMachine(TileEntityElectricMachine tileEntity) {
		super(tileEntity);

		components.add(new GuiEnergyUnitType(this, defaultResource));

		if (tileEntity.hasComponentImplementing(ITileRedstoneControl.class)) {
			TileRedstoneControlComponent tileRedstoneControlComponent = (TileRedstoneControlComponent) tileEntity.getComponentImplementing(ITileRedstoneControl.class);

			components.add(new GuiRedstoneControl<>(this, tileRedstoneControlComponent, defaultResource));
		}
	}
}