package org.halvors.electrometrics.client;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.electrometrics.client.gui.GuiElectricityMeterTest;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
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
			return new GuiElectricityMeterTest(player.inventory, (TileEntityElectricityMeter) tileEntity);

			//return new GuiElectricityMeter((TileEntityElectricityMeter) tileEntity);
		}

		return null;
	}
}