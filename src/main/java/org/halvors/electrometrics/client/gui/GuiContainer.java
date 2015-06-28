package org.halvors.electrometrics.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.IIcon;
import org.halvors.electrometrics.client.gui.component.GuiComponent;
import org.halvors.electrometrics.common.tileentity.TileEntityMachine;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class GuiContainer extends net.minecraft.client.gui.inventory.GuiContainer implements IGui {
    private Set<GuiComponent> guiComponentList = new HashSet<GuiComponent>();

    protected TileEntityMachine tileEntity;

    public GuiContainer(TileEntityMachine tileEntity, Container container) {
        super(container);

        this.tileEntity = tileEntity;
    }

    /**
     * Add a GuiComponent to this screen.
     * @param guiComponent
     * @return guiComponent
     */
    public <T extends GuiComponent> T add(GuiComponent guiComponent) {
        guiComponentList.add(guiComponent);

        return (T) guiComponent;
    }

    public float getNeededScale(String text, int maxX) {
        int length = fontRendererObj.getStringWidth(text);

        if (length <= maxX) {
            return 1;
        } else {
            return (float) maxX / length;
        }
    }

    public void renderScaledText(String text, int x, int y, int color, int maxX) {
        int length = fontRendererObj.getStringWidth(text);

        if (length <= maxX) {
            fontRendererObj.drawString(text, x, y, color);
        } else {
            float scale = (float) maxX / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;

            GL11.glPushMatrix();

            GL11.glScalef(scale, scale, scale);
            fontRendererObj.drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

            GL11.glPopMatrix();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (GuiComponent component : guiComponentList) {
            component.renderForeground(xAxis, yAxis);
        }
    }

    protected boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
        return func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY); // isPointInRegion
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;

        for (GuiComponent component : guiComponentList) {
            component.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (GuiComponent component : guiComponentList) {
            component.preMouseClicked(xAxis, yAxis, button);
        }

        super.mouseClicked(mouseX, mouseY, button);

        for (GuiComponent component : guiComponentList) {
            component.mouseClicked(xAxis, yAxis, button);
        }
    }

    @Override
    protected void drawCreativeTabHoveringText(String text, int x, int y) {
        func_146283_a(Arrays.asList(new String[]{text}), x, y);
    }

    @Override
    protected void func_146283_a(List list, int x, int y) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT + GL11.GL_LIGHTING_BIT);
        super.func_146283_a(list, x, y);
        GL11.glPopAttrib();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {
        super.mouseClickMove(mouseX, mouseY, button, ticks);

        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (GuiComponent component : guiComponentList) {
            component.mouseClickMove(xAxis, yAxis, button, ticks);
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int type) {
        super.mouseMovedOrUp(mouseX, mouseY, type);

        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (GuiComponent component : guiComponentList) {
            component.mouseMovedOrUp(xAxis, yAxis, type);
        }
    }

    public void handleMouse(Slot slot, int slotIndex, int button, int modifier) {
        handleMouseClick(slot, slotIndex, button, modifier);
    }

    @Override
    public void drawTexturedRect(int x, int y, int u, int v, int w, int h) {
        drawTexturedModalRect(x, y, u, v, w, h);
    }

    @Override
    public void drawTexturedRectFromIcon(int x, int y, IIcon icon, int w, int h) {
        drawTexturedModelRectFromIcon(x, y, icon, w, h);
    }

    @Override
    public void displayTooltip(String text, int x, int y) {
        drawCreativeTabHoveringText(text, x, y);
    }

    @Override
    public void displayTooltips(List<String> list, int xAxis, int yAxis) {
        func_146283_a(list, xAxis, yAxis);
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }
}