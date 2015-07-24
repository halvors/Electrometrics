package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.client.gui.component.*;
import org.halvors.electrometrics.common.base.tile.ITileOwnable;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.LanguageUtils;
import org.halvors.electrometrics.common.util.PlayerUtils;
import org.halvors.electrometrics.common.util.energy.EnergyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the GUI of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class GuiElectricityMeter extends GuiComponentContainerScreen {
	private int ticker = 0;

	public GuiElectricityMeter(final TileEntityElectricityMeter tileEntity) {
		super(tileEntity);

		components.add(new GuiOwnerInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(tileEntity.getOwnerName());

				return list;
			}
		}, this, defaultResource));

		// TODO: Get currect energy usage here.
		components.add(new GuiEnergyInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(LanguageUtils.translate("gui.using") + ": " + EnergyUtils.getEnergyDisplay(10) + "/t");
				list.add(LanguageUtils.translate("gui.needed") + ": " + EnergyUtils.getEnergyDisplay(10));

				return list;
			}
		}, this, defaultResource));

		components.add(new GuiEnergyUnitType<>(this, tileEntity, defaultResource));
		components.add(new GuiRedstoneControl<>(this, tileEntity, defaultResource));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		// Create buttons.
		GuiButton resetButton = new GuiButton(0, (guiWidth + xSize) - (60 + 6), (guiHeight + ySize) - (20 + 6), 60, 20, LanguageUtils.translate("gui.reset"));

		// If this has a owner, restrict the reset button to that player.
		if (tileEntity instanceof ITileOwnable) {
			ITileOwnable ownable = (ITileOwnable) tileEntity;

			resetButton.enabled = ownable.isOwner(PlayerUtils.getClientPlayer());
		}

		// Add buttons.
		buttonList.clear();
		buttonList.add(resetButton);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		super.actionPerformed(guiButton);

		if (tileEntity instanceof TileEntityElectricityMeter) {
			TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

			switch (guiButton.id) {
				case 0:
					tileEntityElectricityMeter.setElectricityCount(0);

					// Update the server-side TileEntity.
					PacketHandler.sendToServer(new PacketTileEntity(tileEntityElectricityMeter));
					break;
			}
		}
	}

	@Override
	void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		if (tileEntity instanceof TileEntityElectricityMeter) {
			TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

			// Formatting energy to the correct energy unit.
			String measuredEnergy = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getElectricityCount());
			String storedEnergy = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getEnergyStored());
			String maxOutput = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getMaxEnergyStored());

			fontRendererObj.drawString(LanguageUtils.translate("gui.measured") + ":", 8, ySize/* - 140*/, 0x404040);
			fontRendererObj.drawString(measuredEnergy, 72, ySize - 140, 0x404040);

			// Stored energy.
			fontRendererObj.drawString(LanguageUtils.translate("gui.stored") + ":", 8, ySize/* - 128*/, 0x404040);
			fontRendererObj.drawString(storedEnergy, 72, ySize - 128, 0x404040);

			// Current output.
			fontRendererObj.drawString(LanguageUtils.translate("gui.maxOutput") + ":", 8, ySize/* - 116*/, 0x404040);
			fontRendererObj.drawString(maxOutput + "/t", 72, ySize - 116, 0x404040);

			if (ticker == 0) {
				ticker = 5;
				// Request the latest data from the server-side TileEntity.
				PacketHandler.sendToServer(new PacketRequestData(tileEntityElectricityMeter));
			} else {
				ticker--;
			}
		}

		super.drawGuiScreenForegroundLayer(mouseX, mouseY);
	}
}