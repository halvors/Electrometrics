package org.halvors.electrometrics.common.tile.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.RedstoneControlType;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.component.IComponentContainer;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.network.packet.PacketTileEntity;
import org.halvors.electrometrics.common.tile.TileEntity;

import java.util.List;

public class TileRedstoneControlComponent extends TileComponentBase implements ITileComponent, ITileNetworkable, ITileRedstoneControl {
    // The current RedstoneControlType of this TileEntity.
    private RedstoneControlType redstoneControlType = RedstoneControlType.DISABLED;

    // The current and past redstone state.
    boolean isPowered;
    boolean wasPowered;

    public <T extends TileEntity & IComponentContainer> TileRedstoneControlComponent(T componentContainer) {
        super(componentContainer);
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
    public void onUpdate() {
        // Update wasPowered to the current isPowered.
        wasPowered = isPowered;
    }

    @Override
    public void onNeighborChange() {
        if (!tileEntity.getWorld().isRemote) {
            boolean redstonePower = tileEntity.getWorld().isBlockIndirectlyGettingPowered(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

            if (isPowered != redstonePower) {
                isPowered = redstonePower;

                NetworkHandler.sendToReceivers(new PacketTileEntity(this), tileEntity);
            }
        }
    }

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];
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