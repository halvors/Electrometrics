package org.halvors.electrometrics.client.gui;

import net.minecraft.client.gui.GuiButton;
import org.halvors.electrometrics.common.UnitDisplay;
import org.halvors.electrometrics.common.UnitDisplay.Unit;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

public class GuiElectricityMeter extends GuiScreen {
    private final TileEntityElectricityMeter tileEntity;

    private int ticker = 0;

    public GuiElectricityMeter(TileEntityElectricityMeter tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        buttonList.clear();
        buttonList.add(new GuiButton(0, guiWidth + 128, guiHeight + 64, 60, 20, "Reset"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        super.drawScreen(mouseX, mouseY, partialTick);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        fontRendererObj.drawString("Electricity Meter", guiWidth + 8, guiHeight + 6, 0x404040);

        // Energy count.
        String energy = UnitDisplay.getDisplayShort(tileEntity.getElectricityCount(), Unit.JOULES);

        fontRendererObj.drawString("Energy:", guiWidth + 16, guiHeight + 32, 0x404040);
        fontRendererObj.drawString(energy, guiWidth + 64, guiHeight + 32, 0x404040);

        // Current output.
        fontRendererObj.drawString("Output:", guiWidth + 16, guiHeight + 42, 0x404040);
        fontRendererObj.drawString("0.0 J", guiWidth + 64, guiHeight + 42, 0x404040);

        if (ticker == 0) {
            ticker = 20;
            tileEntity.sync();
        } else {
            ticker--;
        }
    }
}

