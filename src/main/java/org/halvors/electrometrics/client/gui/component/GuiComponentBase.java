package org.halvors.electrometrics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiComponentBase {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    protected final ResourceLocation resource;
    protected final IGui gui;
    protected final ResourceLocation defaultResource;

    protected GuiComponentBase(ResourceLocation resource, IGui gui, ResourceLocation defaultResource) {
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
}