package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.tile.IOwnable;
import org.halvors.electrometrics.common.base.tile.IRedstoneControl;
import org.halvors.electrometrics.common.tile.TileEntityMachine;
import org.halvors.electrometrics.common.util.render.DefaultIcon;
import org.halvors.electrometrics.common.util.render.Renderer;

import java.util.List;

public class BlockMachine extends BlockRotatable {
	BlockMachine(String name) {
		super(name, Material.iron);

		setHardness(2F);
		setResistance(4F);
		setStepSound(soundTypeMetal);
	}

	/*
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativetabs, List list) {
		for (ElectricityMeterTier tier : ElectricityMeterTier.values()) {
			ItemStack itemStack = new ItemStack(this);
			ItemBlockElectricityMeter itemBlockElectricityMeter = (ItemBlockElectricityMeter) itemStack.getItem();
			itemBlockElectricityMeter.setTier(itemStack, tier);

			list.add(itemStack);
		}
	}
	*/

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		super.registerBlockIcons(iconRegister);
		
		// Adding machine types.
		for (MachineType type : MachineType.values()) {
			Renderer.loadDynamicTextures(iconRegister, type.getName(), iconList[type.getMetadata()], defaultIcon);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativetabs, List list) {
		// Making all MachineTypes available in creative mode.
		for (MachineType type : MachineType.values()) {
			list.add(type.getItemStack());
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		MachineType machineType = MachineType.getType(this, metadata);

		return machineType.getTileEntity();
	}

	@Override
	public int damageDropped (int meta) {
		return meta;
	}


	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			// Display a message the the player clicking this block if not the owner.
			if (tileEntity instanceof IOwnable) {
				IOwnable ownable = (IOwnable) tileEntity;

				if (!ownable.isOwner(player)) {
					player.addChatMessage(new ChatComponentText("This block is owned by " + ownable.getOwnerName() + ", you cannot remove this block."));
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (!player.isSneaking()) {
			// Check whether or not this IOwnable has a owner, if not set the current player as owner.
			if (tileEntity instanceof IOwnable) {
				IOwnable ownable = (IOwnable) tileEntity;

				if (!ownable.hasOwner()) {
					ownable.setOwner(player);
				}
			}

			// Open the GUI.
			player.openGui(Electrometrics.getInstance(), 0, world, x, y, z);

			return true;
		}

		return super.onBlockActivated(world, x, y, z, player, facing, playerX, playerY, playerZ);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		// If this TileEntity implements IRedstoneControl, check if it's getting powered.
		if (tileEntity instanceof IRedstoneControl) {
			IRedstoneControl redstoneControl = (IRedstoneControl) tileEntity;
			redstoneControl.setPowered(world.isBlockIndirectlyGettingPowered(x, y, z));
		}

		// Check if this entity is a player.
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			// If this TileEntity implements IOwnable, we set the owner.
			if (tileEntity instanceof IOwnable) {
				IOwnable ownable = (IOwnable) tileEntity;
				ownable.setOwner(player);
			}
		}

		super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			if (tileEntity instanceof TileEntityMachine) {
				TileEntityMachine tileEntityMachine = (TileEntityMachine) tileEntity;
				tileEntityMachine.onNeighborChange();
			}
		}
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		// If this TileEntity implements IOwnable, we check if there is a owner.
		if (tileEntity instanceof IOwnable) {
			IOwnable ownable = (IOwnable) tileEntity;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			return ownable.isOwner(player) ? blockHardness : -1;
		}

		return blockHardness;
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof IOwnable) {
			IOwnable ownable = (IOwnable) tileEntity;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			return ownable.isOwner(player) ? blockResistance : -1;
		}

		return blockResistance;
	}
}