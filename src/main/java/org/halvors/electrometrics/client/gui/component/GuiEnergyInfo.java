package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;

@SideOnly(Side.CLIENT)
public class GuiEnergyInfo extends GuiComponent implements IGuiComponent {
	private final IInfoHandler infoHandler;

	public GuiEnergyInfo(IInfoHandler infoHandler, IGui gui, ResourceLocation defaultResource) {
		super("EnergyInfo.png", gui, defaultResource);

		this.infoHandler = infoHandler;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight, int xSize, int ySize) {
		game.renderEngine.bindTexture(resource);

		gui.drawTexturedRect(guiWidth - 26, guiHeight + 138, 0, 0, 26, 26);

		super.renderBackground(xAxis, yAxis, guiWidth, guiHeight, xSize, ySize);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int xSize, int ySize) {
		if (xAxis >= -21 && xAxis <= -3 && yAxis >= 142 && yAxis <= 160) {
			displayTooltips(infoHandler.getInfo(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis, xSize, ySize);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int xSize, int ySize, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int xSize, int ySize, int button) {

	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

	}

	@Override
	public void mouseMovedOrUp(int x, int y, int type) {

	}
}

