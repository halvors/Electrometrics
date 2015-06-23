package org.halvors.ElectricityMeter.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.halvors.ElectricityMeter.Reference;
import org.halvors.ElectricityMeter.common.tileentity.TileEntityElectricityMeter;
import org.lwjgl.opengl.GL11;

public class GuiElectricityMeter extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(Reference.DOMAIN, "textures/gui/guiElectricityMeter.png");

    private final TileEntityElectricityMeter tileEntity;

    public GuiElectricityMeter(TileEntityElectricityMeter tileEntity) {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        });

        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        fontRendererObj.drawString("Electricity Meter", 8, 6, 0x404040);
        fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);

        // Display current electricity count.
        fontRendererObj.drawString("Count:", 32, ySize - 64, 0x404040);
        fontRendererObj.drawString("Count:", 48, ySize - 64, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);

        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;

        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

        /*
        if (container.tile.theSunIsVisible) {
            drawTexturedModalRect(l + 80, i1 + 45, 176, 0, 14, 14);
        }
        */
    }
}