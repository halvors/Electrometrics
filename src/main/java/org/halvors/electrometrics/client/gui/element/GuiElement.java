package org.halvors.electrometrics.client.gui.element;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.common.util.Rectangle4i;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiElement {
    public static Minecraft game = Minecraft.getMinecraft();

    public ResourceLocation resource;
    public IGui gui;
    public ResourceLocation defaultResource;

    public GuiElement(ResourceLocation resource, IGui gui, ResourceLocation defaultResource) {
        this.resource = resource;
        this.gui = gui;
        this.defaultResource = defaultResource;
    }

    public void displayTooltip(String text, int xAxis, int yAxis) {
        gui.displayTooltip(text, xAxis, yAxis);
    }

    public void displayTooltips(List<String> list, int xAxis, int yAxis) {
        gui.displayTooltips(list, xAxis, yAxis);
    }

    /*
    public void offsetX(int xSize) {
        if (gui instanceof GuiContainer) {
            try {
                int size = (Integer) MekanismUtils.getPrivateValue(gui, GuiContainer.class, OebfuscatedNames.GuiContainer_xSiz);
                MekanismUtils.setPrivateValue(gui, size + xSize, GuiContainer.class, ObfuscatedNames.GuiContainer_xSize);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void offsetY(int ySize) {
        if (gui instanceof GuiContainer) {
            try {
                int size = (Integer) MekanismUtils.getPrivateValue(gui, GuiContainer.class, ObfuscatedNames.GuiContainer_ySize);
                MekanismUtils.setPrivateValue(gui, size + ySize, GuiContainer.class, ObfuscatedNames.GuiContainer_ySize);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void offsetLeft(int guiLeft) {
        if (gui instanceof GuiContainer) {
            try {
                int left = (Integer) MekanismUtils.getPrivateValue(gui, GuiContainer.class, ObfuscatedNames.GuiContainer_guiLeft);
                System.out.println(left + " " + guiLeft);
                MekanismUtils.setPrivateValue(gui, left + guiLeft, GuiContainer.class, ObfuscatedNames.GuiContainer_guiLeft);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void offsetTop(int guiTop) {
        if (gui instanceof GuiContainer) {
            try {
                int top = (Integer) MekanismUtils.getPrivateValue(gui, GuiContainer.class, ObfuscatedNames.GuiContainer_guiTop);
                MekanismUtils.setPrivateValue(gui, top + guiTop, GuiContainer.class, ObfuscatedNames.GuiContainer_guiTop);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    */

    public void renderScaledText(String text, int x, int y, int color, int maxX) {
        int length = getFontRenderer().getStringWidth(text);

        if (length <= maxX) {
            getFontRenderer().drawString(text, x, y, color);
        } else {
            float scale = (float) maxX / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;

            GL11.glPushMatrix();

            GL11.glScalef(scale, scale, scale);
            getFontRenderer().drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

            GL11.glPopMatrix();
        }
    }

    public FontRenderer getFontRenderer() {
        return gui.getFontRenderer();
    }

    public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

    }

    public void mouseMovedOrUp(int x, int y, int type) {

    }

    public abstract Rectangle4i getBounds(int guiWidth, int guiHeight);

    public abstract void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight);

    public abstract void renderForeground(int xAxis, int yAxis);

    public abstract void preMouseClicked(int xAxis, int yAxis, int button);

    public abstract void mouseClicked(int xAxis, int yAxis, int button);
}