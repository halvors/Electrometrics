package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

import java.util.Random;

/**
 * This is the Block of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class BlockElectricityMeter extends BlockBasic {
	@SideOnly(Side.CLIENT)
	public static IIcon icon, frontIcon;

	public BlockElectricityMeter() {
		super(Material.iron);

		setBlockName("blockElectricityMeter");
		setHardness(3.5F);
		setResistance(8F);
		//setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityElectricityMeter();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(Reference.PREFIX + "blockElectricityMeter");
		frontIcon = iconRegister.registerIcon(Reference.PREFIX + "blockElectricityMeterFront");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch (side) {
			case 3:
				return frontIcon;

			default:
				return icon;
		}
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

		/*
		TileEntityEnergyCube tileEntity = (TileEntityEnergyCube)world.getTileEntity(x, y, z);
		ItemStack itemStack = new ItemStack(MekanismBlocks.EnergyCube);

		IEnergyCube energyCube = (IEnergyCube)itemStack.getItem();
		energyCube.setEnergyCubeTier(itemStack, tileEntity.tier);

		IEnergizedItem energizedItem = (IEnergizedItem)itemStack.getItem();
		energizedItem.setEnergy(itemStack, tileEntity.electricityStored);

		ISustainedInventory inventory = (ISustainedInventory)itemStack.getItem();
		inventory.setInventory(((ISustainedInventory)tileEntity).getInventory(), itemStack);
		 */
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
		if (player.isSneaking()) {
			return false;
		}

		if (world.isRemote) {
			// Open the GUI.
			player.openGui(Electrometrics.getInstance(), 0, world, x, y, z);
		}

		return true;
	}
}
