package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.Location;

import java.util.ArrayList;

/**
 * This is the GUI of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
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
        buttonList.add(new GuiButton(0, guiWidth + 116, guiHeight + 96, 60, 20, "Reset"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case 0:
                tileEntity.setElectricityCount(0);

                // Update the server-side TileEntity.
                PacketHandler.getNetwork().sendToServer(new PacketTileEntity(new Location(tileEntity), tileEntity.getPacketData(new ArrayList())));
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        super.drawScreen(mouseX, mouseY, partialTick);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        // Formatting energy to the correct energy unit.
        String energyCount = Electrometrics.getEnergyDisplay(tileEntity.getElectricityCount());
        String maxOutput = Electrometrics.getEnergyDisplay(tileEntity.getStorage().getMaxEnergyStored());

        fontRendererObj.drawString("Measured energy:", guiWidth + 6, guiHeight + 32, 0x404040);
        fontRendererObj.drawString(energyCount, guiWidth + 64, guiHeight + 32, 0x404040);

        // Current output.
        fontRendererObj.drawString("Max output:", guiWidth + 6, guiHeight + 42, 0x404040);
        fontRendererObj.drawString(maxOutput, guiWidth + 64, guiHeight + 42, 0x404040);

        if (ticker == 0) {
            ticker = 5;
            // Request the latest data from the server-side TileEntity.
            PacketHandler.getNetwork().sendToServer(new PacketRequestData(new Location(tileEntity)));
        } else {
            ticker--;
        }
    }
}

