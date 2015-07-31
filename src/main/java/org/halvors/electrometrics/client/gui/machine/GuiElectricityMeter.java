package org.halvors.electrometrics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.client.gui.GuiComponentContainerScreen;
import org.halvors.electrometrics.client.gui.component.*;
import org.halvors.electrometrics.common.base.tile.ITileOwnable;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.network.packet.PacketRequestData;
import org.halvors.electrometrics.common.network.packet.PacketTileEntity;
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
	private final TileEntityElectricityMeter tileEntityElectricityMeter;
	private int ticker = 0;

	public GuiElectricityMeter(final TileEntityElectricityMeter tileEntityElectricityMeter) {
		super(tileEntityElectricityMeter);

		this.tileEntityElectricityMeter = tileEntityElectricityMeter;

		components.add(new GuiOwnerInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(tileEntityElectricityMeter.getOwnerName());

				return list;
			}
		}, this, defaultResource));

		// TODO: Get currect energy usage here.
		components.add(new GuiEnergyInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(LanguageUtils.localize("gui.stored") + ": " + EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getEnergyStored()));
				list.add(LanguageUtils.localize("gui.maxOutput") + ": " + EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getElectricTier().getMaxTransfer()));

				return list;
			}
		}, this, defaultResource));

		components.add(new GuiEnergyUnitType(this, defaultResource));
		components.add(new GuiRedstoneControl<>(this, tileEntityElectricityMeter, defaultResource));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		// Create buttons.
		GuiButton resetButton = new GuiButton(0, guiWidth + 6, (guiHeight + ySize) - (20 + 6), 60, 20, LanguageUtils.localize("gui.reset"));
		GuiButton unclaimButton = new GuiButton(1, (guiWidth + xSize) - (60 + 6), (guiHeight + ySize) - (20 + 6), 60, 20, LanguageUtils.localize("gui.release"));

		// If this has a owner, restrict the reset button to that player.
		if (tileEntity instanceof ITileOwnable) {
			ITileOwnable ownable = (ITileOwnable) tileEntity;
			EntityPlayer player = PlayerUtils.getClientPlayer();

			unclaimButton.enabled = ownable.isOwner(player);
			resetButton.enabled = ownable.isOwner(player);
		}

		// Add buttons.
		buttonList.clear();
		buttonList.add(resetButton);
		buttonList.add(unclaimButton);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		switch (guiButton.id) {
			case 0:
				tileEntityElectricityMeter.setElectricityCount(0);

				// Update the server-side TileEntity.
				NetworkHandler.sendToServer(new PacketTileEntity(tileEntityElectricityMeter));
				break;

			case 1:
				// Claim or unclaim this here.
				break;
		}
	}

	@Override
	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		// Formatting energy to the correct energy unit.
		String measuredEnergy = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getElectricityCount());
		String storedEnergy = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getEnergyStored());

		int x = 6;
		int y = ySize / 2;

		drawString(LanguageUtils.localize("gui.measured") + ":", x, y - 24);
		drawString(measuredEnergy, x + 64, y - 24);
		drawString(LanguageUtils.localize("gui.stored") + ":", x, y - 12);
		drawString(storedEnergy, x + 64, y - 12);

		if (ticker > 0) {
			ticker--;
		} else {
			ticker = 5;

			// Request the latest data from the server-side TileEntity.
			NetworkHandler.sendToServer(new PacketRequestData(tileEntityElectricityMeter));
		}

		super.drawGuiScreenForegroundLayer(mouseX, mouseY);
	}
}