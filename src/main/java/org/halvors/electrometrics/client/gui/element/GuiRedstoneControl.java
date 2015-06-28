package org.halvors.electrometrics.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.common.tileentity.IRedstoneControl;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl extends GuiElement {
    private TileEntity tileEntity;

    public GuiRedstoneControl(IGuiWrapper gui, TileEntity tile, ResourceLocation def) {
        super(new ResourceLocation("/gui/element/", "GuiRedstoneControl.png"), gui, def);

        tileEntity = tile;
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + 176, guiHeight + 138, 26, 26);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        mc.renderEngine.bindTexture(RESOURCE);

        guiObj.drawTexturedRect(guiWidth + 176, guiHeight + 138, 0, 0, 26, 26);

        IRedstoneControl control = (IRedstoneControl) tileEntity;
        int renderX = 26 + (18 * control.getControlType().ordinal());

        if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
            guiObj.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 0, 18, 18);
        } else {
            guiObj.drawTexturedRect(guiWidth + 179, guiHeight + 142, renderX, 18, 18, 18);
        }

        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        mc.renderEngine.bindTexture(RESOURCE);

        IRedstoneControl control = (IRedstoneControl) tileEntity;

        if(xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
            displayTooltip(control.getControlType().getDisplay(), xAxis, yAxis);
        }

        mc.renderEngine.bindTexture(defaultLocation);
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {}

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        IRedstoneControl control = (IRedstoneControl) tileEntity;

        if (button == 0) {
            if (xAxis >= 179 && xAxis <= 197 && yAxis >= 142 && yAxis <= 160) {
                RedstoneControl current = control.getControlType();
                int ordinalToSet = current.ordinal() < (RedstoneControl.values().length-1) ? current.ordinal()+1 : 0;
                if(ordinalToSet == RedstoneControl.PULSE.ordinal() && !control.canPulse()) ordinalToSet = 0;

                SoundHandler.playSound("gui.button.press");
                Mekanism.packetHandler.sendToServer(new RedstoneControlMessage(Coord4D.get(tileEntity), RedstoneControl.values()[ordinalToSet]));
            }
        }
    }
}
