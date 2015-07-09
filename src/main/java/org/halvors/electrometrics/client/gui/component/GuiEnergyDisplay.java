package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.client.sound.SoundHandler;
import org.halvors.electrometrics.common.util.UnitDisplay.Unit;
import org.halvors.electrometrics.common.util.render.Rectangle4i;

@SideOnly(Side.CLIENT)
public class GuiEnergyDisplay extends GuiComponent implements IGuiComponent {
	private final TileEntity tileEntity;
    private Unit energyType = Electrometrics.energyType;

	public GuiEnergyDisplay(IGui gui, TileEntity tileEntity, ResourceLocation defaultResource) {
		super(new ResourceLocation(Reference.DOMAIN, "gui/elements/guiEnergyDisplay.png"), gui, defaultResource);

		this.tileEntity = tileEntity;
	}

	@Override
	public Rectangle4i getBounds(int guiWidth, int guiHeight) {
		return new Rectangle4i(guiWidth + 176, guiHeight + 138, 26, 26);
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		mc.renderEngine.bindTexture(resource);

		gui.drawTexturedRect(guiWidth + 176, guiHeight + 138, 0, 0, 26, 26);

		int renderX = 26 + (18 * energyType.ordinal());

		if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
			gui.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 18, 18, 18);
		}

		super.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis) {
		mc.renderEngine.bindTexture(resource);

		if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
			displayTooltip(energyType.getName(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int button) {
		switch (button) {
			case 0:
				if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
                    Unit current = energyType;
                    int ordinalToSet = current.ordinal() < (Unit.values().length - 1) ? current.ordinal() + 1 : 0;

                    SoundHandler.playSound("gui.button.press");

					// Set energy type to display here.
                    Electrometrics.energyType = Unit.values()[ordinalToSet];
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
