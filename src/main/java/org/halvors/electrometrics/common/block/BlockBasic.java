package org.halvors.electrometrics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.halvors.electrometrics.ElectroMetrics;
import org.halvors.electrometrics.common.tileentity.TileEntityBasic;

public class BlockBasic extends BlockContainer {
    protected BlockBasic(Material material) {
        super(material);

        setCreativeTab(ElectroMetrics.tabElectroMetrics);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityBasic();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileEntityBasic) {
            TileEntityBasic tileEntityBasic = (TileEntityBasic) tileEntity;

            int side = MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            int height = Math.round(entity.rotationPitch);
            int change = 3;

            if (tileEntityBasic.canSetFacing(0) && tileEntityBasic.canSetFacing(1)) {
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

            tileEntityBasic.setFacing((short) change);
        }
    }
}
