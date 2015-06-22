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
        this.drawDefaultBackground();
    }
}
