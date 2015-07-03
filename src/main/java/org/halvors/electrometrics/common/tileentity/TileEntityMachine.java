package org.halvors.electrometrics.common.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;

import java.util.List;

/**
 * This is a basic TileEntity that is meant to be extended by other TileEntities.
 *
 * @author halvors
 */
public abstract class TileEntityMachine extends TileEntityBasic implements IRotatable, INetworkable {
	// The direction this TileEntity's block is facing.
	private int facing;

	// The direction this TileEntity's block is facing, client side.
	@SideOnly(Side.CLIENT)
	private int clientFacing;

	TileEntityMachine(String name) {
		super(name);
	}

	@Override
	public void validate() {
		super.validate();

		if (worldObj.isRemote) {
			PacketHandler.sendToServer(new PacketRequestData(this));
		}
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
		if (clientFacing != facing) {
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
		}

		/*
		if(!worldObj.isRemote) {
			markDirty();
		}
		*/
	}

	public abstract void onNeighborChange();
}
