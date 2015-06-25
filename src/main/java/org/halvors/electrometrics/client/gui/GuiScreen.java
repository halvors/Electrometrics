package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Reference;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreen extends net.minecraft.client.gui.GuiScreen {
    private static final ResourceLocation texture = new ResourceLocation(Reference.DOMAIN, "gui/guiScreenBlank.png");

    // The name of this GuiScreen.
    private String name;

    // The size dimensions of this GuiScreen.
    protected int xSize = 176;
    protected int ySize = 166;

    public GuiScreen(String name) {
        this.name = name;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawScreen(mouseX, mouseY, partialTick);

        // Display the name of this GuiScreen.
        fontRendererObj.drawString(getName(), (guiWidth + (xSize / 2)) - fontRendererObj.getStringWidth(getName()), guiHeight + 6, 0x404040);
    }

    public String getName() {
        return name;
    }
}
