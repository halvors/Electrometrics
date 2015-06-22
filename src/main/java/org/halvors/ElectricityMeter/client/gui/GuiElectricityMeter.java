package org.halvors.ElectricityMeter.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.halvors.ElectricityMeter.Reference;

public class GuiElectricityMeter extends GuiScreen {
    private static final ResourceLocation texture = new ResourceLocation(Reference.DOMAIN, "textures/gui/guiElectricityMeter.png");

    public GuiElectricityMeter() {

    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        fontRendererObj.drawString("Electricity Meter", 8, 6, 0x404040);
    }

    /*
    Override
    protected void drawGuiContainerForegroundLayer(int p1, int p2) {
        fontRendererObj.drawString(type.mainType.friendlyName, 8, 6, 0x404040);
        fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(gui);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        if (container.tile.theSunIsVisible) {
            drawTexturedModalRect(l + 80, i1 + 45, 176, 0, 14, 14);
        }
    }
     */
}
