package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;
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
		super("EnergyUnitType.png", gui, defaultResource);

		this.tileEntity = tileEntity;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);

		gui.drawTexturedRect(xOrigin + guiWidth, yOrigin + guiHeight - 2 - 26, 0, 0, 26, 26);

		int renderX = 26 + (18 * tileEntity.getControlType().ordinal());

		int x = xOrigin + guiWidth + 3;
		int y = yOrigin + guiHeight - 2 - 26 + 4;

		if (xAxis >= x && xAxis <= x + 17 && yAxis >= y && yAxis <= y + 17) {
			gui.drawTexturedRect(x, y, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(x, y, renderX, 18, 18, 18);
		}

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		int x = guiWidth + 3;
		int y = guiHeight - 2 - 26 + 4;

		game.renderEngine.bindTexture(resource);

		//game.renderEngine.bindTexture(ResourceUtils.getResource(ResourceType.GUI_ELEMENT, "lol"));
		//gui.drawTexturedRect(x, y, 0, 0, 18, 18);

		if (xAxis >= x && xAxis <= x + 17 && yAxis >= y && yAxis <= y + 17) {
			displayTooltip(tileEntity.getControlType().getDisplay(), xAxis, yAxis);
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
				int x = guiWidth + 3;
				int y = guiHeight - 2 - 26 + 4;

				if (xAxis >= x && xAxis <= x + 17 && yAxis >= y && yAxis <= y + 17) {
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
