package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tileentity.INetworkable;
import org.halvors.electrometrics.common.tileentity.IRedstoneControl;
import org.halvors.electrometrics.common.tileentity.RedstoneControlType;
import org.halvors.electrometrics.common.util.render.Rectangle4i;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl extends GuiComponent {
    private TileEntity tileEntity;

    public GuiRedstoneControl(IGui gui, TileEntity tileEntity, ResourceLocation defaultResource) {
        super(new ResourceLocation(Reference.DOMAIN, "gui/elements/guiRedstoneControl.png"), gui, defaultResource);

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

        if (tileEntity instanceof IRedstoneControl) {
            IRedstoneControl redstoneControl = (IRedstoneControl) tileEntity;
            int renderX = 26 + (18 * redstoneControl.getControlType().ordinal());

            if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
                gui.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 0, 18, 18);
            } else {
                gui.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 18, 18, 18);
            }
        }

        mc.renderEngine.bindTexture(defaultResource);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        mc.renderEngine.bindTexture(resource);

        if (tileEntity instanceof IRedstoneControl) {
            IRedstoneControl redstoneControl = (IRedstoneControl) tileEntity;

            if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
                displayTooltip(redstoneControl.getControlType().getDisplay(), xAxis, yAxis);
            }
        }

        mc.renderEngine.bindTexture(defaultResource);
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        if (tileEntity instanceof IRedstoneControl) {
            IRedstoneControl redstoneControl = (IRedstoneControl) tileEntity;

            if (button == 0) {
                if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
                    RedstoneControlType current = redstoneControl.getControlType();
                    int ordinalToSet = current.ordinal() < (RedstoneControlType.values().length - 1) ? current.ordinal() + 1 : 0;

                    if (ordinalToSet == RedstoneControlType.PULSE.ordinal() && !redstoneControl.canPulse()) {
                        ordinalToSet = 0;
                    }

                    //SoundHandler.playSound("gui.button.press");

                    // Set the redstone control type.
                    redstoneControl.setControlType(RedstoneControlType.values()[ordinalToSet]);

                    if (tileEntity instanceof INetworkable) {
                        INetworkable networkable = (INetworkable) tileEntity;

                        // Send a update packet to the server.
                        PacketHandler.sendToServer(new PacketTileEntity(networkable));
                    }
                }
            }
        }
    }
}
