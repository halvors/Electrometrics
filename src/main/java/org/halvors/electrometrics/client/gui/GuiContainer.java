package org.halvors.electrometrics.client.gui;

import net.minecraft.inventory.Container;

public class GuiContainer extends net.minecraft.client.gui.inventory.GuiContainer {
    public GuiContainer(Container container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        /*
        fontRendererObj.drawString("Electricity Meter", 8, 6, 0x404040);
        fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);

        // Display current electricity count.
        fontRendererObj.drawString("Count:", 32, ySize - 64, 0x404040);
        fontRendererObj.drawString("Count:", 48, ySize - 64, 0x404040);
        */
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        /*
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        if (container.tile.theSunIsVisible) {
            drawTexturedModalRect(l + 80, i1 + 45, 176, 0, 14, 14);
        }
        */
    }
}
