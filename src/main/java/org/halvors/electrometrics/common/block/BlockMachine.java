package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.tileentity.*;
import org.halvors.electrometrics.common.util.Orientation;
import org.halvors.electrometrics.common.util.render.DefaultIcon;
import org.halvors.electrometrics.common.util.render.Renderer;

public class BlockMachine extends BlockBasic {
	private final String name;

	@SideOnly(Side.CLIENT)
	private IIcon baseIcon;

	@SideOnly(Side.CLIENT)
	private final IIcon[] iconList = new IIcon[16];

	BlockMachine(String name) {
		super(Material.iron);

		this.name = name;

		setBlockName(name);
		setHardness(2F);
		setResistance(4F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		baseIcon = iconRegister.registerIcon(Reference.PREFIX + name);

		Renderer.loadDynamicTextures(iconRegister, name, iconList, DefaultIcon.getAll(baseIcon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		// Check if this implements IRotatable.
		if (tileEntity instanceof IRotatable) {
			IRotatable rotatable = (IRotatable) tileEntity;
			boolean isActive = false;

			// Check if this implements IActiveState, if it do we get the state from it.
			if (tileEntity instanceof IActiveState) {
				IActiveState activeState = (IActiveState) tileEntity;

				isActive = activeState.isActive();
			}

			return iconList[Orientation.getBaseOrientation(side, rotatable.getFacing() + (isActive ? 6 : 0))];
		}

		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		// Workaround to for when block is not rendered in world, by swapping the front and back sides.
		switch (side) {
			case 2: // Back (South)
				side = 3; // Fix.
				break;

			case 3: // Front (North)
				side = 2; // Fix.
				break;
		}

		return iconList[side];
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			// Display a message the the player clicking this block if not the owner.
			if (tileEntity instanceof IOwnable) {
				IOwnable ownable = (IOwnable) tileEntity;

				if (!ownable.isOwner(player)) {
					EntityPlayerMP playerOwner = ownable.getOwner();
					String name = playerOwner != null ? playerOwner.getDisplayName() : ownable.getOwnerName();

					player.addChatMessage(new ChatComponentText("This block is owned by " + name + ", you cannot remove this block."));
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
		if (!player.isSneaking()) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

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

			rotatable.setFacing((short) change);
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
				rotatable.setFacing((short) axis.ordinal());

				return true;
			}
		}

		return super.rotateBlock(world, x, y, z, axis);
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
}