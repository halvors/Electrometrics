package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.UnitDisplay;
import org.halvors.electrometrics.common.UnitDisplay.Unit;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

@SideOnly(Side.CLIENT)
public class GuiElectricityMeter extends GuiScreen {
    private final TileEntityElectricityMeter tileEntity;

    private int ticker = 0;

    public GuiElectricityMeter(TileEntityElectricityMeter tileEntity) {
        super("Electricity Meter");

        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        // Add buttons.
        buttonList.clear();
        buttonList.add(new GuiButton(0, guiWidth + 108, guiHeight + 56, 60, 20, "Reset"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 0:
                tileEntity.setElectricityCount(0);
                tileEntity.sendData();
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        super.drawScreen(mouseX, mouseY, partialTick);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        // Formatting energy to the correct energy unit.
        String energy = Electrometrics.getEnergyDisplay(tileEntity.getElectricityCount());
        String maxOutput = Electrometrics.getEnergyDisplay(tileEntity.getStorage().getMaxEnergyStored());

        fontRendererObj.drawString("Energy:", guiWidth, guiHeight + 32, 0x404040);
        fontRendererObj.drawString(energy, guiWidth + 48, guiHeight + 32, 0x404040);

        // Current output.
        fontRendererObj.drawString("Max output:", guiWidth, guiHeight + 40, 0x404040);
        fontRendererObj.drawString(maxOutput, guiWidth + 48, guiHeight + 40, 0x404040);

        if (ticker == 0) {
            ticker = 5;
            tileEntity.requestData();
        } else {
            ticker--;
        }
    }
}

