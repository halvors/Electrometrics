package org.halvors.electrometrics.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.lwjgl.opengl.GL11;

public class GuiElectricityMeter1 extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(Reference.DOMAIN, "textures/gui/guiElectricityMeter.png");

    private final TileEntityElectricityMeter tileEntity;

    public GuiElectricityMeter1(TileEntityElectricityMeter tileEntity) {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        });

        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRendererObj.drawString("Electricity Meter", 8, 6, 0x404040);
        fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);

        // Display current electricity count.
        fontRendererObj.drawString("Count:", 32, ySize - 64, 0x404040);
        fontRendererObj.drawString("Count:", 48, ySize - 64, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        /*
        if (container.tile.theSunIsVisible) {
            drawTexturedModalRect(l + 80, i1 + 45, 176, 0, 14, 14);
        }
        */
    }
}