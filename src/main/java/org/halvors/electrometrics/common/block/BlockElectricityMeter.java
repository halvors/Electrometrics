package org.halvors.electrometrics.common.block;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

import java.util.Random;

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
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityElectricityMeter();
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return null;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if (!player.capabilities.isCreativeMode && !world.isRemote && canHarvestBlock(player, world.getBlockMetadata(x, y, z))) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			if (tileEntity instanceof TileEntityElectricityMeter) {
				TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;
				ItemStack itemStack = new ItemStack(Electrometrics.blockElectricityMeter);

				ItemBlockElectricityMeter itemBlockElectricityMeter = (ItemBlockElectricityMeter) itemStack.getItem();
				itemBlockElectricityMeter.setElectricityCount(itemStack, tileEntityElectricityMeter.getElectricityCount());

				// Create the entityItem.
				float motion = 0.7F;
				double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
				double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
				double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
				EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);

				// Spawn the entityItem in the world.
				world.spawnEntityInWorld(entityItem);
			}
		}

		return world.setBlockToAir(x, y, z);
	}
}
