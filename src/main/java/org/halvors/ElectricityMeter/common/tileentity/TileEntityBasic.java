package org.halvors.ElectricityMeter.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBasic extends TileEntity {
    // The direction this block is facing.
    public int facing;

    public TileEntityBasic() {

    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTags) {
        super.readFromNBT(nbtTags);

        facing = nbtTags.getInteger("facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTags) {
        super.writeToNBT(nbtTags);

        nbtTags.setInteger("facing", facing);
    }

    /**
     * Whether or not this block's orientation can be changed to a specific direction. True by default.
     * @param facing - facing to check
     * @return if the block's orientation can be changed
     */
    public boolean canSetFacing(int facing) {
        return true;
    }

    public short getFacing() {
        return (short)facing;
    }

    public void setFacing(short facing) {
        if (canSetFacing(facing)) {
            this.facing = facing;
        }
    }
}
