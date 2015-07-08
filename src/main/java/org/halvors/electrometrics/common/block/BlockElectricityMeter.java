package org.halvors.electrometrics.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.tile.TileEntityElectricityMeter;

/**
 * This is the Block of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class BlockElectricityMeter extends BlockMachine {
	public BlockElectricityMeter() {
		super("blockElectricityMeter");
	}

	@Override
	 public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if (!player.capabilities.isCreativeMode && !world.isRemote && canHarvestBlock(player, world.getBlockMetadata(x, y, z))) {
			dismantleBlock(world, x, y, z, false);
		}

		return world.setBlockToAir(x, y, z);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) world.getTileEntity(x, y, z);
		ItemStack itemStack = new ItemStack(Electrometrics.blockElectricityMeter);

		ItemBlockElectricityMeter itemBlockElectricityMeter = (ItemBlockElectricityMeter) itemStack.getItem();
		itemBlockElectricityMeter.setTier(itemStack, tileEntityElectricityMeter.getTier());
		itemBlockElectricityMeter.setElectricityCount(itemStack, tileEntityElectricityMeter.getElectricityCount());
		itemBlockElectricityMeter.setElectricityStored(itemStack, tileEntityElectricityMeter.getStorage().getEnergyStored());

		return itemStack;
	}
}