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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.client.render.DefaultIcon;
import org.halvors.electrometrics.client.render.Renderer;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.Tier.ElectricityMeterTier;
import org.halvors.electrometrics.common.base.tile.IOwnable;
import org.halvors.electrometrics.common.base.tile.IRedstoneControl;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.TileEntityElectricBlock;

import java.util.List;

/**
 * Block class for handling multiple machine block IDs.
 *
 * 0: Basic Electricity Meter
 * 1: Advanced Electricity Meter
 * 2: Elite Electricity Meter
 * 3: Ultimate Electricity Meter
 * 4: Creative Electricity Meter
 */
public class BlockMachine extends BlockRotatable {
    private final MachineType machineType;

	BlockMachine(MachineType machineType) {
		super(machineType.getUnlocalizedName(), Material.iron);

        this.machineType = machineType;

		setHardness(2F);
		setResistance(4F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return machineType.getTileEntity();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		IIcon baseIcon = iconRegister.registerIcon(Reference.PREFIX + "Machine");
		defaultIcon = DefaultIcon.getAll(baseIcon);

		// Adding machine types.
		for (MachineType machineType : MachineType.values()) {
			Renderer.loadDynamicTextures(iconRegister, machineType.getUnlocalizedName(), iconList[machineType.getMetadata()], defaultIcon);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativetabs, List list) {
		// Making all MachineTypes available in creative mode.
		for (MachineType machineType : MachineType.values()) {
			switch (machineType) {
				case BASIC_ELECTRICITY_METER:
				case ADVANCED_ELECTRICITY_METER:
				case ELITE_ELECTRICITY_METER:
				case ULTIMATE_ELECTRICITY_METER:
				case CREATIVE_ELECTRICITY_METER:
					ItemStack itemStack = machineType.getItemStack();
					ItemBlockElectricityMeter itemBlockElectricityMeter = (ItemBlockElectricityMeter) itemStack.getItem();
					itemBlockElectricityMeter.setTier(itemStack, ElectricityMeterTier.getFromMachineType(machineType));

					list.add(itemStack);
					break;

				default:
					list.add(machineType.getItemStack());
					break;
			}
		}
	}

	@Override
	public int damageDropped (int metadata) {
		return metadata;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

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
		TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

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
		TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

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
			TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

			if (tileEntity instanceof TileEntityElectricBlock) {
				TileEntityElectricBlock tileEntityElectricBlock = (TileEntityElectricBlock) tileEntity;
				tileEntityElectricBlock.onNeighborChange();
			}
		}
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

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
		TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

		if (tileEntity instanceof IOwnable) {
			IOwnable ownable = (IOwnable) tileEntity;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			return ownable.isOwner(player) ? blockResistance : -1;
		}

		return blockResistance;
	}
}