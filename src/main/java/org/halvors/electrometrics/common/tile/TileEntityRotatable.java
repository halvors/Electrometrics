package org.halvors.electrometrics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileRotatable;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;

import java.util.List;

public class TileEntityRotatable extends TileEntity implements ITileNetworkable, ITileRotatable {
	// The direction this TileEntity's block is facing.
	protected int facing;

	// The direction this TileEntity's block is facing, client side.
	private int clientFacing;

	protected TileEntityRotatable(String inventoryName) {
		super(inventoryName);
	}

	@Override
	public void validate() {
		super.validate();

		if (worldObj.isRemote) {
			PacketHandler.sendToServer(new PacketRequestData(this));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		facing = nbtTagCompound.getInteger("facing");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setInteger("facing", facing);
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		facing = dataStream.readInt();

		// Check if client is in sync with the server, if not update it.
		if (clientFacing != facing) {
			clientFacing = facing;

			// Update the block's rotation.
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

			// Update potentially connected redstone blocks.
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
		}
	}

	@Override
	public List<Object> getPacketData(List<Object> objects) {
		objects.add(facing);

		return objects;
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
		}

		if (worldObj.isRemote) {
			PacketHandler.sendToServer(new PacketTileEntity(this));
		}

		// Update the block's rotation.
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		// Update potentially connected redstone blocks.
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());

		/*
		if (!worldObj.isRemote || clientFacing != facing) {
			clientFacing = facing;

			PacketHandler.sendToReceivers(new PacketTileEntity(this), this);
		}
		*/
	}
}