package org.halvors.electrometrics.common.tileentity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.util.Utils;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This is the TileEntity of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class TileEntityElectricityMeter extends TileEntityEnergyProvider implements INetworkable, IOwnable, IRedstoneControl, IActiveState {
	// The UUID of the player owning this.
	private UUID owner;

	// The name of the player owning this.
	private String ownerName;

	// The current RedstoneControlType of this TileEntity.
	private RedstoneControlType redstoneControlType = RedstoneControlType.DISABLED;

	// Whether or not this TileEntity's block is in it's active state.
	private boolean isActive;

	// The client's current active state.
	@SideOnly(Side.CLIENT)
	private boolean clientIsActive;

	// The amount of energy that has passed thru.
	private double electricityCount;

	public boolean isPowered;
	public boolean wasPowered;

	public TileEntityElectricityMeter() {
		super("Electricity Meter", 25600, 25600, 25600);
	}

	@Override
	public void validate() {
		super.validate();

		if (worldObj.isRemote) {
			PacketHandler.sendToServer(new PacketRequestData(this));
		}
	}

	@Override
	public void updateEntity() {
		wasPowered = isPowered;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		owner = UUID.fromString(nbtTags.getString("owner"));
		ownerName = nbtTags.getString("ownerName");
		redstoneControlType = RedstoneControlType.values()[nbtTags.getInteger("redstoneControlType")];
		isActive = nbtTags.getBoolean("isActive");
		electricityCount = nbtTags.getDouble("electricityCount");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		nbtTags.setString("owner", owner != null ? owner.toString() : "");
		nbtTags.setString("ownerName", !ownerName.isEmpty() ? ownerName : "");
		nbtTags.setInteger("redstoneControlType", redstoneControlType.ordinal());
		nbtTags.setBoolean("isActive", isActive);
		nbtTags.setDouble("electricityCount", electricityCount);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		// Add the amount of energy we're extracting to the counter.
		if (!simulate) {
			electricityCount += maxExtract;
		}

		return super.extractEnergy(from, maxExtract, simulate);
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		owner = UUID.fromString(ByteBufUtils.readUTF8String(dataStream));
		ownerName = ByteBufUtils.readUTF8String(dataStream);
		redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];
		isActive = dataStream.readBoolean();

		// Check if client is in sync with the server, if not update it.
		if (clientIsActive != isActive) {
			clientIsActive = isActive;

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		electricityCount = dataStream.readDouble();
	}

	@Override
	public ArrayList<Object> getPacketData(ArrayList<Object> data) {
		super.getPacketData(data);

		data.add(owner != null ? owner.toString() : "");
		data.add(!ownerName.isEmpty() ? ownerName : "");
		data.add(redstoneControlType.ordinal());
		data.add(isActive);
		data.add(electricityCount);

		return data;
	}

	@Override
	public boolean hasOwner() {
		return owner != null;
	}

	@Override
	public boolean isOwner(EntityPlayer player) {
		return owner.equals(player.getPersistentID());
	}

	@Override
	public EntityPlayerMP getOwner() {
		return Utils.getPlayerFromUUID(owner);
	}

	@Override
	public String getOwnerName() {
		return ownerName;
	}

	@Override
	public void setOwner(EntityPlayer player) {
		this.owner = player.getPersistentID();
		this.ownerName = player.getDisplayName();
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

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Returns the amount of energy that this block has totally received.
	 */
	public double getElectricityCount() {
		return electricityCount;
	}

	/**
	 * Sets the amount of energy that this block has totally received.
	 */
	public void setElectricityCount(double electricityCount) {
		this.electricityCount = electricityCount;
	}

	public void onNeighborChange(Block block) {
		if (!worldObj.isRemote) {
			boolean power = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

			if (isPowered != power) {
				isPowered = power;

				PacketHandler.sendToReceivers(new PacketTileEntity(this), this);
			}
		}
	}
}
