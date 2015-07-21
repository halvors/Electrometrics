package org.halvors.electrometrics.common.tile.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.base.tile.RedstoneControlType;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tile.TileEntityComponentContainer;

import java.util.List;

public class TileRedstoneControlComponent extends TileComponent implements ITileNetworkableComponent, ITileRedstoneControl {
    // The current RedstoneControlType of this TileEntity.
    private RedstoneControlType redstoneControlType = RedstoneControlType.DISABLED;

    // The current and past redstone state.
    boolean isPowered;
    boolean wasPowered;

    public TileRedstoneControlComponent(TileEntityComponentContainer tileEntity) {
        super(tileEntity);
    }

    @Override
    public void onUpdate() {
        // Update wasPowered to the current isPowered.
        wasPowered = isPowered;
    }

    @Override
    public void onNeighborChange() {
        if (!tileEntity.getWorldObj().isRemote) {
            boolean redstonePower = tileEntity.getWorldObj().isBlockIndirectlyGettingPowered(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

            if (isPowered != redstonePower) {
                isPowered = redstonePower;

                PacketHandler.sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        redstoneControlType = RedstoneControlType.values()[nbtTagCompound.getInteger("redstoneControlType")];
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("redstoneControlType", redstoneControlType.ordinal());
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];

        System.out.println("RedstoneControlType is: " + redstoneControlType);
    }

    @Override
    public List<Object> getPacketData(List<Object> list) {
        list.add(redstoneControlType.ordinal());

        return list;
    }

    @Override
    public RedstoneControlType getControlType() {
        return redstoneControlType;
    }

    @Override
    public void setControlType(RedstoneControlType redstoneControlType) {
        this.redstoneControlType = redstoneControlType;
    }

    @Override
    public boolean isPowered() {
        return isPowered;
    }

    @Override
    public void setPowered(boolean isPowered) {
        this.isPowered = isPowered;
    }

    @Override
    public boolean wasPowered() {
        return wasPowered;
    }

    @Override
    public boolean canPulse() {
        return false;
    }
}