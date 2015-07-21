package org.halvors.electrometrics.common.tile.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.RedstoneControllableType;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControllable;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.tile.TileEntityComponentContainer;

import java.util.List;

public class TileRedstoneControlComponent extends TileComponent implements ITileNetworkableComponent, ITileRedstoneControllable {
    // The current RedstoneControllableType of this TileEntity.
    private RedstoneControllableType redstoneControllableType = RedstoneControllableType.DISABLED;

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
        redstoneControllableType = RedstoneControllableType.values()[nbtTagCompound.getInteger("redstoneControllableType")];
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("redstoneControllableType", redstoneControllableType.ordinal());
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        redstoneControllableType = RedstoneControllableType.values()[dataStream.readInt()];

        System.out.println("RedstoneControllableType is: " + redstoneControllableType);
    }

    @Override
    public List<Object> getPacketData(List<Object> list) {
        list.add(redstoneControllableType.ordinal());

        return list;
    }

    @Override
    public RedstoneControllableType getControlType() {
        return redstoneControllableType;
    }

    @Override
    public void setControlType(RedstoneControllableType redstoneControllableType) {
        this.redstoneControllableType = redstoneControllableType;
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