package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.client.gui.component.GuiEnergyInfo;
import org.halvors.electrometrics.client.gui.component.GuiOwnerInfo;
import org.halvors.electrometrics.client.gui.component.GuiRedstoneControl;
import org.halvors.electrometrics.client.gui.component.IInfoHandler;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tileentity.IOwnable;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the GUI of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class GuiElectricityMeter extends GuiScreen {
	private int ticker = 0;

	public GuiElectricityMeter(final TileEntityElectricityMeter tileEntity) {
		super(tileEntity);

		add(new GuiOwnerInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				List<String> list = new ArrayList<>();
				list.add(tileEntity.getOwnerName());

				return list;
			}
		}, this, defaultResource));

		add(new GuiEnergyInfo(new IInfoHandler() {
			@Override
			public List<String> getInfo() {
				String multiplier = Utils.getEnergyDisplay(10);

				List<String> list = new ArrayList<>();
				list.add("Using: " + multiplier + "/t");
				list.add("Needed: " + Utils.getEnergyDisplay(10));

				return list;
			}
		}, this, defaultResource));

		add(new GuiRedstoneControl(this, tileEntity, defaultResource));
	}

	@Override
	public void initGui() {
		super.initGui();

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		// Create buttons.
		GuiButton resetButton = new GuiButton(0, guiWidth + 110, guiHeight + 60, 60, 20, "Reset");

		// If this has a owner, restrict the reset button to that player.
		if (tileEntity instanceof IOwnable) {
			IOwnable ownable = (IOwnable) tileEntity;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			resetButton.enabled = ownable.isOwner(player);
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
					PacketHandler.sendToServer(new PacketTileEntity(tileEntity));
					break;
			}
		}
	}

	@Override
	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		if (tileEntity instanceof TileEntityElectricityMeter) {
			TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

			// Formatting energy to the correct energy unit.
			String energyCount = Utils.getEnergyDisplay(tileEntityElectricityMeter.getElectricityCount());
			String maxOutput = Utils.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getMaxEnergyStored());

			fontRendererObj.drawString("Measured:", 8, ySize - 140, 0x404040);
			fontRendererObj.drawString(energyCount, 72, ySize - 140, 0x404040);

			// Current output.
			fontRendererObj.drawString("Max output:", 8, ySize - 128, 0x404040);
			fontRendererObj.drawString(maxOutput + "/t", 72, ySize - 128, 0x404040);

			if (ticker == 0) {
				ticker = 5;
				// Request the latest data from the server-side TileEntity.
				PacketHandler.sendToServer(new PacketRequestData(tileEntity));
			} else {
				ticker--;
			}
		}

		super.drawGuiScreenForegroundLayer(mouseX, mouseY);
	}
}