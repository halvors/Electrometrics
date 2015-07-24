package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.client.render.Rectangle4i;
import org.halvors.electrometrics.client.sound.SoundHandler;
import org.halvors.electrometrics.common.base.RedstoneControlType;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tile.TileEntity;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl<T extends TileEntity & ITileRedstoneControl & ITileNetworkable> extends GuiComponent implements IGuiComponent {
	private final T tileEntity;

	public GuiRedstoneControl(IGui gui, T tileEntity, ResourceLocation defaultResource) {
		super("RedstoneControl.png", gui, defaultResource);

		this.tileEntity = tileEntity;
	}

	@Override
	public Rectangle4i getBounds(int guiWidth, int guiHeight) {
		return new Rectangle4i(guiWidth/* + 176*/, guiHeight - 26 - 2/* + 138*/, 26, 26);
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight, int xSize, int ySize) {
		int x = guiWidth + xSize;
		int y = guiHeight + ySize - 26 - 2;
		int renderX = 26 + (18 * tileEntity.getControlType().ordinal());

		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(x, y, 0, 0, 26, 26);

		if (xAxis >= x + 3 && xAxis <= x + 21 && yAxis >= y + 4 && yAxis <= y + 22) {
			gui.drawTexturedRect(x + 3, y + 4, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(x + 3, y + 4, renderX, 18, 18, 18);
		}

		super.renderBackground(xAxis, yAxis, guiWidth, guiHeight, xSize, ySize);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int xSize, int ySize) {
		int x = xSize;
		int y = ySize - 26 - 2;

		game.renderEngine.bindTexture(resource);

		if (xAxis >= x + 3 && xAxis <= x + 21 && yAxis >= y + 4 && yAxis <= y + 22) {
		//if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
			displayTooltip(tileEntity.getControlType().getDisplay(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis, xSize, ySize);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int button) {
		switch (button) {
			case 0:
				if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
					RedstoneControlType current = tileEntity.getControlType();
					int ordinalToSet = current.ordinal() < (RedstoneControlType.values().length - 1) ? current.ordinal() + 1 : 0;

					if (ordinalToSet == RedstoneControlType.PULSE.ordinal() && !tileEntity.canPulse()) {
						ordinalToSet = 0;
					}

					SoundHandler.playSound("gui.button.press");

					// Set the redstone control type.
					tileEntity.setControlType(RedstoneControlType.values()[ordinalToSet]);

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
