package org.halvors.electrometrics.common.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.INetworkable;
import org.halvors.electrometrics.common.base.tile.IRotatable;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;

import java.util.List;

/**
 * This is a basic TileEntity that is meant to be extended by other TileEntities.
 *
 * @author halvors
 */
public class TileEntityElectricMachine extends TileEntity implements INetworkable, IRotatable {
	// The direction this TileEntity's block is facing.
	protected int facing;

	// The direction this TileEntity's block is facing, client side.
	@SideOnly(Side.CLIENT)
	protected int clientFacing;

    // The current and past redstone state.
    protected boolean isPowered;
    protected boolean wasPowered;

	TileEntityElectricMachine(String name) {
		super(name);
	}

	@Override
	public void validate() {
		super.validate();

        PacketHandler.sendToServer(new PacketRequestData(this));
	}

    @Override
    public void updateEntity() {
        super.updateEntity();

        // Update wasPowered to the current isPowered.
        wasPowered = isPowered;
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

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		facing = dataStream.readInt();

		// Check if client is in sync with the server, if not update it.
		if (worldObj.isRemote && clientFacing != facing) {
			clientFacing = facing;

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public List<Object> getPacketData(List<Object> list) {
		list.add(facing);

		return list;
	}

	@Override
	public boolean canSetFacing(int facing) {
		return true;
	}

	@Override
	public int getFacing() {
		return facing;
	}

	@Override
	public void setFacing(int facing) {
		if (canSetFacing(facing)) {
			this.facing = facing;

			PacketHandler.sendToServer(new PacketRequestData(this));
		}
	}

    public void onNeighborChange() {
        if (!worldObj.isRemote) {
            boolean redstonePower = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

            if (isPowered != redstonePower) {
                isPowered = redstonePower;

                PacketHandler.sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }
}
