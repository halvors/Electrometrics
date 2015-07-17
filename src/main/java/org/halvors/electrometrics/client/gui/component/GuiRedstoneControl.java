package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.client.render.Rectangle4i;
import org.halvors.electrometrics.client.sound.SoundHandler;
import org.halvors.electrometrics.common.base.tile.INetworkable;
import org.halvors.electrometrics.common.base.tile.IRedstoneControl;
import org.halvors.electrometrics.common.base.tile.RedstoneControlType;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tile.TileEntity;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl<T extends TileEntity & IRedstoneControl & INetworkable> extends GuiComponent implements IGuiComponent {
	private final IRedstoneControl redstoneControl;
	private final INetworkable networkable;

	public GuiRedstoneControl(IGui gui, T tileEntity, ResourceLocation defaultResource) {
		super("guiRedstoneControl.png", gui, defaultResource);

		this.redstoneControl = tileEntity;
		this.networkable = tileEntity;
	}

	@Override
	public Rectangle4i getBounds(int guiWidth, int guiHeight) {
		return new Rectangle4i(guiWidth + 176, guiHeight + 138, 26, 26);
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);

		gui.drawTexturedRect(guiWidth + 176, guiHeight + 138, 0, 0, 26, 26);

		int renderX = 26 + (18 * redstoneControl.getControlType().ordinal());

		if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
			gui.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 18, 18, 18);
		}

        super.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis) {
		game.renderEngine.bindTexture(resource);

		if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
			displayTooltip(redstoneControl.getControlType().getDisplay(), xAxis, yAxis);
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
					RedstoneControlType current = redstoneControl.getControlType();
					int ordinalToSet = current.ordinal() < (RedstoneControlType.values().length - 1) ? current.ordinal() + 1 : 0;

					if (ordinalToSet == RedstoneControlType.PULSE.ordinal() && !redstoneControl.canPulse()) {
						ordinalToSet = 0;
					}

					SoundHandler.playSound("gui.button.press");

					// Set the redstone control type.
					redstoneControl.setControlType(RedstoneControlType.values()[ordinalToSet]);

					// Send a update packet to the server.
					PacketHandler.sendToServer(new PacketTileEntity(networkable));
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
