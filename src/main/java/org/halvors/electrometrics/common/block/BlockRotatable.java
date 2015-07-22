package org.halvors.electrometrics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.base.tile.ITileRotatable;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.MachineUtils;

abstract class BlockRotatable extends BlockTextured {
    BlockRotatable(String name, Material material) {
        super(name, material);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

        // If this TileEntity implements ITileRotatable, we do our rotations.
        if (tileEntity instanceof ITileRotatable) {
            ITileRotatable rotatable = (ITileRotatable) tileEntity;

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
                    case 0:
                        change = 2;
                        break;
                    case 1:
                        change = 5;
                        break;
                    case 2:
                        change = 3;
                        break;
                    case 3:
                        change = 4;
                        break;
                }
            }

            rotatable.setFacing(change);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
        TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

        // Handle wrenching.
        if (player.getCurrentEquippedItem() != null && MachineUtils.hasUsableWrench(player, x, y, z)) {
            if (player.isSneaking()) {
                dismantleBlock(world, x, y, z, false);

                return true;
            } else {
                if (tileEntity instanceof ITileRotatable) {
                    ITileRotatable rotatable = (ITileRotatable) tileEntity;
                    int change = ForgeDirection.ROTATION_MATRIX[ForgeDirection.UP.ordinal()][rotatable.getFacing()];

                    rotatable.setFacing(change);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ForgeDirection[] getValidRotations(World world, int x, int y, int z) {
        TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);
        ForgeDirection[] valid = new ForgeDirection[6];

        // If this TileEntity implements ITileRotatable, we do our rotations.
        if (tileEntity instanceof ITileRotatable) {
            ITileRotatable rotatable = (ITileRotatable) tileEntity;

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
        TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

        // If this TileEntity implements ITileRotatable, we do our rotations.
        if (tileEntity instanceof ITileRotatable) {
            ITileRotatable rotatable = (ITileRotatable) tileEntity;

            if (rotatable.canSetFacing(axis.ordinal())) {
                rotatable.setFacing(axis.ordinal());

                return true;
            }
        }

        return super.rotateBlock(world, x, y, z, axis);
    }

    ItemStack dismantleBlock(World world, int x, int y, int z, boolean returnBlock) {
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
}
