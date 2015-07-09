package org.halvors.electrometrics.client;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.base.MachineType;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        MachineType type = MachineType.getType(block, metadata);

        return type.getGui();
	}
}