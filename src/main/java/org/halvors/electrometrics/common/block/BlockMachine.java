package org.halvors.electrometrics.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.tileentity.IOwnable;
import org.halvors.electrometrics.common.tileentity.IRedstoneControl;
import org.halvors.electrometrics.common.tileentity.IRotatable;
import org.halvors.electrometrics.common.tileentity.TileEntityMachine;
import org.halvors.electrometrics.common.util.Utils;

public class BlockMachine extends BlockTextured {
	BlockMachine(String name) {
		super(name, Material.iron);

		setHardness(2F);
		setResistance(4F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
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

		// Handle wrenching.
		if (player.getCurrentEquippedItem() != null && Utils.hasUsableWrench(player, x, y, z)) {
			if (player.isSneaking()) {
				dismantleBlock(world, x, y, z, false);

				return true;
			}

			if (tileEntity instanceof IRotatable) {
				IRotatable rotatable = (IRotatable) tileEntity;

				int change = ForgeDirection.ROTATION_MATRIX[ForgeDirection.UP.ordinal()][rotatable.getFacing()];

				rotatable.setFacing(change);
				world.notifyBlocksOfNeighborChange(x, y, z, this);

				return true;
			}
		}

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

		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		// If this TileEntity implements IRotatable, we do our rotations.
		if (tileEntity instanceof IRotatable) {
			IRotatable rotatable = (IRotatable) tileEntity;

			int side = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			int height = Math.round(entity.rotationPitch);
			int change = 3;

			if (rotatable.canSetFacing(0) && rotatable.canSetFacing(1)) {
				if (height >= 65) {
					change = 1;
				} else if (height <= -65) {
					change = 0;
				}
			}

			if (change != 0 && change != 1) {
				switch (side) {
					case 0: change = 2; break;
					case 1: change = 5; break;
					case 2: change = 3; break;
					case 3: change = 4; break;
				}
			}

			rotatable.setFacing(change);
		}

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
	}

	@Override
	public ForgeDirection[] getValidRotations(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		ForgeDirection[] valid = new ForgeDirection[6];

		// If this TileEntity implements IRotatable, we do our rotations.
		if (tileEntity instanceof IRotatable) {
			IRotatable rotatable = (IRotatable) tileEntity;

			for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
				if (rotatable.canSetFacing(direction.ordinal())) {
					valid[direction.ordinal()] = direction;
				}
			}
		}

		return valid;
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		// If this TileEntity implements IRotatable, we do our rotations.
		if (tileEntity instanceof IRotatable) {
			IRotatable rotatable = (IRotatable) tileEntity;

			if (rotatable.canSetFacing(axis.ordinal())) {
				rotatable.setFacing(axis.ordinal());

				return true;
			}
		}

		return super.rotateBlock(world, x, y, z, axis);
	}

	public ItemStack dismantleBlock(World world, int x, int y, int z, boolean returnBlock) {
		ItemStack itemStack = getPickBlock(null, world, x, y, z, null);
		world.setBlockToAir(x, y, z);

		if (!returnBlock) {
			float motion = 0.7F;
			double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
			double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
			double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

			EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);
			world.spawnEntityInWorld(entityItem);
		}

		return itemStack;
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