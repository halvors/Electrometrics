package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.client.gui.component.GuiEnergyInfo;
import org.halvors.electrometrics.client.gui.component.GuiOwnerInfo;
import org.halvors.electrometrics.client.gui.component.GuiRedstoneControl;
import org.halvors.electrometrics.client.gui.component.IInfoHandler;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tileentity.IOwnable;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.tileentity.TileEntityMachine;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the GUI of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class GuiElectricityMeter extends GuiScreen {
    private GuiButton resetButton;
    private int ticker = 0;

    public GuiElectricityMeter(TileEntityMachine tileEntity) {
        super(tileEntity);

        add(new GuiOwnerInfo(new IInfoHandler() {
            @Override
            public List<String> getInfo() {
                List<String> list = new ArrayList<String>();
                list.add("halvorshalvors");

                return list;
            }
        }, this, tileEntity.getGuiResource()));

        add(new GuiEnergyInfo(new IInfoHandler() {
            @Override
            public List<String> getInfo() {
                String multiplier = Electrometrics.getEnergyDisplay(10);

                List<String> list = new ArrayList<String>();
                list.add("Using: " + multiplier + "/t");
                list.add("Needed: " + Electrometrics.getEnergyDisplay(10));

                return list;
            }
        }, this, tileEntity.getGuiResource()));

        add(new GuiRedstoneControl(this, tileEntity, tileEntity.getGuiResource()));
    }

    @Override
    public void initGui() {
        super.initGui();

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        // Create buttons.
        resetButton = new GuiButton(0, guiWidth + 110, guiHeight + 60, 60, 20, "Reset");

        // If this has a owner, restrict the reset button to that player.
        if (tileEntity instanceof IOwnable) {
            IOwnable ownable = (IOwnable) tileEntity;
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            resetButton.enabled = ownable.isOwner(player);
        }

        // Add buttons.
        buttonList.clear();
        buttonList.add(resetButton);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        super.actionPerformed(guiButton);

        if (tileEntity instanceof TileEntityElectricityMeter) {
            TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

            switch (guiButton.id) {
                case 0:
                    tileEntityElectricityMeter.setElectricityCount(0);

                    // Update the server-side TileEntity.
                    PacketHandler.getNetwork().sendToServer(new PacketTileEntity(tileEntity));
                    break;
            }
        }
    }

    @Override
    protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
        if (tileEntity instanceof TileEntityElectricityMeter) {
            TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;

            // Formatting energy to the correct energy unit.
            String energyCount = Electrometrics.getEnergyDisplay(tileEntityElectricityMeter.getElectricityCount());
            String maxOutput = Electrometrics.getEnergyDisplay(tileEntityElectricityMeter.getStorage().getMaxEnergyStored());

            fontRendererObj.drawString("Measured:", 8, ySize - 140, 0x404040);
            fontRendererObj.drawString(energyCount, 72, ySize - 140, 0x404040);

            // Current output.
            fontRendererObj.drawString("Max output:", 8, ySize - 128, 0x404040);
            fontRendererObj.drawString(maxOutput + "/t", 72, ySize - 128, 0x404040);

            if (ticker == 0) {
                ticker = 5;
                // Request the latest data from the server-side TileEntity.
                PacketHandler.getNetwork().sendToServer(new PacketRequestData(tileEntity));
            } else {
                ticker--;
            }
        }

        super.drawGuiScreenForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiScreenBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(tileEntity.getGuiResource());

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawGuiScreenBackgroundLayer(partialTick, mouseX, mouseY);
    }
}