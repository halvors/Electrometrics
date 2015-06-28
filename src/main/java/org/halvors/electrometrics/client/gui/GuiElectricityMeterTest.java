package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.client.gui.element.GuiEnergyInfo;
import org.halvors.electrometrics.client.gui.element.GuiOwnerInfo;
import org.halvors.electrometrics.client.gui.element.GuiRedstoneControl;
import org.halvors.electrometrics.client.gui.element.IInfoHandler;
import org.halvors.electrometrics.common.tileentity.TileEntityMachine;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiElectricityMeterTest extends Gui {
    public TileEntityMachine tileEntity;

    public GuiElectricityMeterTest(InventoryPlayer inventory, TileEntityMachine tileEntity) {
        super(tileEntity, new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        });

        this.tileEntity = tileEntity;

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

        /*
        guiElements.add(new GuiRedstoneControl(this, tileEntity, tileEntity.guiLocation));
        guiElements.add(new GuiUpgradeTab(this, tileEntity, tileEntity.guiLocation));
        guiElements.add(new GuiSideConfigurationTab(this, tileEntity, tileEntity.guiLocation));
        guiElements.add(new GuiTransporterConfigTab(this, 34, tileEntity, tileEntity.guiLocation));
        guiElements.add(new GuiPowerBar(this, tileEntity, tileEntity.guiLocation, 164, 15));
        guiElements.add(new GuiEnergyInfo(new IInfoHandler() {
            @Override
            public List<String> getInfo()
            {
                String multiplier = MekanismUtils.getEnergyDisplay(tileEntity.energyPerTick);
                return ListUtils.asList(LangUtils.localize("gui.using") + ": " + multiplier + "/t", LangUtils.localize("gui.needed") + ": " + MekanismUtils.getEnergyDisplay(tileEntity.getMaxEnergy()-tileEntity.getEnergy()));
            }
        }, this, tileEntity.guiLocation));

        guiElements.add(new GuiSlot(SlotType.INPUT, this, tileEntity.guiLocation, 55, 16));
        guiElements.add(new GuiSlot(SlotType.POWER, this, tileEntity.guiLocation, 55, 52).with(SlotOverlay.POWER));
        guiElements.add(new GuiSlot(SlotType.OUTPUT_LARGE, this, tileEntity.guiLocation, 111, 30));

        guiElements.add(new GuiProgress(new IProgressInfoHandler()
        {
            @Override
            public double getProgress()
            {
                return tileEntity.getScaledProgress();
            }
        }, getProgressType(), this, tileEntity.guiLocation, 77, 37));
        */
    }

    /*
    public ProgressBar getProgressType()
    {
        return ProgressBar.BLUE;
    }
    */

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tileEntity.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tileEntity.getName()) / 2), 6, 0x404040);
        fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(tileEntity.getGuiResource());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}