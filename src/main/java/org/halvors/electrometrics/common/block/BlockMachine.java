package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.tileentity.IActiveState;
import org.halvors.electrometrics.common.tileentity.IOwnable;
import org.halvors.electrometrics.common.tileentity.IRotatable;
import org.halvors.electrometrics.common.tileentity.TileEntityMachine;
import org.halvors.electrometrics.common.util.Orientation;
import org.halvors.electrometrics.common.util.render.DefaultIcon;
import org.halvors.electrometrics.common.util.render.Renderer;

public class BlockMachine extends BlockBasic {
	private String name;

	@SideOnly(Side.CLIENT)
	private IIcon baseIcon;

	@SideOnly(Side.CLIENT)
	private IIcon[] icons = new IIcon[16];

	protected BlockMachine(String name, Material material) {
		super(material);

		this.name = name;

		setBlockName(name);
		setHardness(3.5F);
		setResistance(8F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityMachine();
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

		Renderer.loadDynamicTextures(iconRegister, name, icons, DefaultIcon.getAll(baseIcon));
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

			return icons[Orientation.getBaseOrientation(side, rotatable.getFacing() + (isActive ? 6 : 0))];
		}

		return baseIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[side];
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			// Display a message the the player clicking this block if not the owner.
			if (tileEntity instanceof IOwnable) {
				IOwnable ownable = (IOwnable) tileEntity;

				if (!ownable.isOwner(player)) {
					player.addChatMessage(new ChatComponentText("Only the owner can remove this block."));
				}
			}
		}
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
}