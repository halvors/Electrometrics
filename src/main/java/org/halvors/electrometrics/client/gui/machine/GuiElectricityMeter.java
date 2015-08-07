package org.halvors.electrometrics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.client.gui.component.GuiEnergyInfo;
import org.halvors.electrometrics.client.gui.component.GuiOwnerInfo;
import org.halvors.electrometrics.client.gui.component.IInfoHandler;
import org.halvors.electrometrics.common.base.tile.ITileOwnable;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.network.packet.PacketTileEntityElectricityMeter;
import org.halvors.electrometrics.common.tile.component.TileOwnableComponent;
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
public class GuiElectricityMeter extends GuiElectricMachine {
	private final TileEntityElectricityMeter tileEntityElectricityMeter;
	private int ticker = 0;

	public GuiElectricityMeter(final TileEntityElectricityMeter tileEntity) {
		super(tileEntity);

		this.tileEntityElectricityMeter = tileEntity;

		if (tileEntity.hasComponent(TileOwnableComponent.class)) {
			final ITileOwnable tileOwnable = (ITileOwnable) tileEntity.getComponent(TileOwnableComponent.class);

			components.add(new GuiOwnerInfo(new IInfoHandler() {
				@Override
				public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(tileOwnable.getOwnerName());

				return list;
				}
			}, this, defaultResource));
		}

		// TODO: Get currect energy usage here.
		components.add(new GuiEnergyInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(LanguageUtils.localize("gui.stored") + ": " + EnergyUtils.getEnergyDisplay(tileEntity.getStorage().getEnergyStored()));
				list.add(LanguageUtils.localize("gui.maxOutput") + ": " + EnergyUtils.getEnergyDisplay(tileEntity.getElectricTier().getMaxTransfer()));

				return list;
			}
		}, this, defaultResource));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		// Create buttons.
		GuiButton resetButton = new GuiButton(0, guiWidth + 6, (guiHeight + ySize) - (20 + 6), 60, 20, LanguageUtils.localize("gui.reset"));

		// If this has a owner, restrict the reset button to that player.
		if (tileEntity instanceof ITileOwnable) {
			ITileOwnable ownable = (ITileOwnable) tileEntity;
			EntityPlayer player = PlayerUtils.getClientPlayer();

			resetButton.enabled = ownable.isOwner(player);
		}

		// Add buttons.
		buttonList.clear();
		buttonList.add(resetButton);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		switch (guiButton.id) {
			case 0:
				// Update the server-side TileEntity.
				NetworkHandler.sendToServer(new PacketTileEntityElectricityMeter(tileEntityElectricityMeter, PacketTileEntityElectricityMeter.PacketType.RESET));
				break;
		}
	}

	@Override
	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		// Formatting energy to the correct energy unit.
		String measuredEnergy = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getElectricityCount());
		String storedEnergy = EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getEnergyStored());

		int x = 6 + 12;
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
			NetworkHandler.sendToServer(new PacketTileEntityElectricityMeter(tileEntityElectricityMeter, PacketTileEntityElectricityMeter.PacketType.REQUEST));
		}

		super.drawGuiScreenForegroundLayer(mouseX, mouseY);
	}
}