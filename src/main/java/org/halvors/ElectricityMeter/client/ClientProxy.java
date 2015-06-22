package org.halvors.ElectricityMeter.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.ElectricityMeter.client.gui.GuiElectricityMeter;
import org.halvors.ElectricityMeter.common.CommonProxy;
import org.halvors.ElectricityMeter.common.tileentity.TileEntityElectricityMeter;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        // This is for rendering entities and so forth later on
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileEntityElectricityMeter) {
            return new GuiElectricityMeter();
        }

        return null;
    }
}