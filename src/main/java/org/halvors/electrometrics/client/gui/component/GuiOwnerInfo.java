package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.common.util.render.Rectangle4i;

@SideOnly(Side.CLIENT)
public class GuiOwnerInfo extends GuiComponent implements IGuiComponent {
	private final IInfoHandler infoHandler;

	public GuiOwnerInfo(IInfoHandler infoHandler, IGui gui, ResourceLocation defaultResource) {
		super(new ResourceLocation(Reference.DOMAIN, "gui/elements/guiOwnerInfo.png"), gui, defaultResource);

		this.infoHandler = infoHandler;
	}

	@Override
	public Rectangle4i getBounds(int guiWidth, int guiHeight) {
		return new Rectangle4i(guiWidth - 26, guiHeight + 1, 26, 26);
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		mc.renderEngine.bindTexture(resource);

		gui.drawTexturedRect(guiWidth - 26, guiHeight + 1, 0, 0, 26, 26);

        super.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis) {
		if (xAxis >= -21 && xAxis <= -3 && yAxis >= 5 && yAxis <= 23) {
			displayTooltips(infoHandler.getInfo(), xAxis, yAxis);
		}

        super.renderForeground(xAxis, yAxis);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int button) {

	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

	}

	@Override
	public void mouseMovedOrUp(int x, int y, int type) {

	}
}

