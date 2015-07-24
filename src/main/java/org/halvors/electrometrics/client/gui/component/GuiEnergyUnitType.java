package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.client.sound.SoundHandler;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.energy.Unit;

@SideOnly(Side.CLIENT)
public class GuiEnergyUnitType<T extends TileEntity & ITileNetworkable> extends GuiComponent implements IGuiComponent {
	private final T tileEntity;

	public GuiEnergyUnitType(IGui gui, T tileEntity, ResourceLocation defaultResource) {
		super("EnergyUnitType.png", gui, defaultResource);

		this.tileEntity = tileEntity;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(xOrigin + 176, yOrigin + 2, 0, 0, 26, 26);

		int renderX = 26 + (18 * Electrometrics.energyUnitType.ordinal());

		if (xAxis >= 179 && xAxis <= 197 && yAxis >= 6 && yAxis <= 24) {
			gui.drawTexturedRect(xOrigin + 179, yOrigin + 6, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(xOrigin + 179, yOrigin + 6, renderX, 18, 18, 18);
		}

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);

		if (xAxis >= 179 && xAxis <= 197 && yAxis >= 6 && yAxis <= 24) {
			displayTooltip(Electrometrics.energyUnitType.getName(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis, guiWidth, guiHeight);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button) {
		switch (button) {
			case 0:
				if (xAxis >= 179 && xAxis <= 197 && yAxis >= 6 && yAxis <= 24) {
					Unit current = Electrometrics.energyUnitType;
					int ordinalToSet = current.ordinal() < (Unit.values().length - 1) ? current.ordinal() + 1 : 0;

					SoundHandler.playSound("gui.button.press");

					// Set energy unit type to use.
					Electrometrics.energyUnitType = Unit.values()[ordinalToSet];

					// Send a update packet to the server.
					PacketHandler.sendToServer(new PacketTileEntity(tileEntity));
				}
				break;
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

	}

	@Override
	public void mouseMovedOrUp(int x, int y, int type) {

	}
}
