package org.halvors.electrometrics.client.gui;

import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Reference;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends net.minecraft.client.gui.GuiScreen {
    private static final ResourceLocation texture = new ResourceLocation(Reference.DOMAIN, "gui/guiBlank.png");

    protected int xSize = 176;
    protected int ySize = 166;

    public GuiScreen() {

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawScreen(mouseX, mouseY, partialTick);
    }
}
