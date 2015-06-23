package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

@SideOnly(Side.CLIENT)
public class GuiElectricityMeter extends GuiScreen {
    private final TileEntityElectricityMeter tileEntity;

    public GuiElectricityMeter(TileEntityElectricityMeter tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        buttonList.clear();
        buttonList.add(new GuiButton(0, guiWidth + 55, guiHeight + 68, 60, 20, ""));
    }

    @Override
    public void updateScreen() {
        /*
        passwordField.updateCursorCounter();

        if (ticker > 0) {
            ticker--;
        } else {
            displayText = EnumColor.BRIGHT_GREEN + "Enter password";
        }
        */
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        fontRendererObj.drawString("Password", guiWidth + 64, guiHeight + 5, 0x404040);
        fontRendererObj.drawString("Enter:", guiWidth + 45, guiHeight + 40, 0x404040);

        /*
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        */

        super.drawScreen(mouseX, mouseY, partialTick);
    }
}

