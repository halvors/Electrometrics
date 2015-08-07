package org.halvors.electrometrics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.client.gui.component.GuiEnergyUnitType;
import org.halvors.electrometrics.client.gui.component.GuiRedstoneControl;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.tile.component.TileRedstoneControlComponent;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricMachine;

@SideOnly(Side.CLIENT)
public class GuiElectricMachine extends GuiMachine {
	protected GuiElectricMachine(TileEntityElectricMachine tileEntity) {
		super(tileEntity);

		components.add(new GuiEnergyUnitType(this, defaultResource));

		System.out.println("TileRedstoneControlComponent is: " + tileEntity.hasComponent(TileRedstoneControlComponent.class));

		if (tileEntity.hasComponent(TileRedstoneControlComponent.class)) {
			TileRedstoneControlComponent tileRedstoneControlComponent = (TileRedstoneControlComponent) tileEntity.getComponent(TileRedstoneControlComponent.class);

			components.add(new GuiRedstoneControl<>(this, tileRedstoneControlComponent, defaultResource));
		}
	}
}