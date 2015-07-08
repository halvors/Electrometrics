package org.halvors.electrometrics.common.tile;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.base.Tier.ElectricityMeterTier;
import org.halvors.electrometrics.common.base.tile.*;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.network.PacketTileEntity;
import org.halvors.electrometrics.common.util.Utils;

import java.util.EnumSet;
import java.util.List;
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

	private ElectricityMeterTier electricityMeterTier = ElectricityMeterTier.BASIC;

	// The amount of energy that has passed thru.
	private double electricityCount;

	// The current and past redstone state.
	private boolean isPowered;
	private boolean wasPowered;

	public TileEntityElectricityMeter() {
		super(ElectricityMeterTier.BASIC.getBaseTier().getName() + " Electricity Meter", ElectricityMeterTier.BASIC.getMaxEnergy(), ElectricityMeterTier.BASIC.getMaxTransfer(), ElectricityMeterTier.BASIC.getMaxTransfer());
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
		super.updateEntity();

		// Update wasPowered to the current isPowered.
		wasPowered = isPowered;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		owner = UUID.fromString(nbtTags.getString("owner"));
		ownerName = nbtTags.getString("ownerName");
		redstoneControlType = RedstoneControlType.values()[nbtTags.getInteger("redstoneControlType")];
		isActive = nbtTags.getBoolean("isActive");

		electricityMeterTier = ElectricityMeterTier.values()[nbtTags.getInteger("tier")];
		setName(electricityMeterTier.getBaseTier().getName());
		storage.setCapacity(electricityMeterTier.getMaxEnergy());
		storage.setMaxTransfer(electricityMeterTier.getMaxTransfer());

		electricityCount = nbtTags.getDouble("electricityCount");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		nbtTags.setString("owner", owner != null ? owner.toString() : "");
		nbtTags.setString("ownerName", !ownerName.isEmpty() ? ownerName : "");
		nbtTags.setInteger("redstoneControlType", redstoneControlType.ordinal());
		nbtTags.setBoolean("isActive", isActive);

		nbtTags.setInteger("tier", electricityMeterTier.ordinal());
		nbtTags.setDouble("electricityCount", electricityCount);
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

		electricityMeterTier = ElectricityMeterTier.values()[dataStream.readInt()];
		electricityCount = dataStream.readDouble();
	}

	@Override
	public List<Object> getPacketData(List<Object> list) {
		super.getPacketData(list);

		list.add(owner != null ? owner.toString() : "");
		list.add(!ownerName.isEmpty() ? ownerName : "");
		list.add(redstoneControlType.ordinal());
		list.add(isActive);

		list.add(electricityMeterTier.ordinal());
		list.add(electricityCount);

		return list;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (getExtractingSides().contains(from)) {
			// Add the amount of energy we're extracting to the counter.
			if (!simulate) {
				electricityCount += maxExtract;
			}
		}

		return super.extractEnergy(from, maxExtract, simulate);
	}

	@Override
	public EnumSet<ForgeDirection> getExtractingSides() {
		return EnumSet.of(ForgeDirection.getOrientation(facing).getRotation(ForgeDirection.DOWN));
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
	public EntityPlayer getOwner() {
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

	public ElectricityMeterTier getTier() {
		return electricityMeterTier;
	}

	public void setTier(ElectricityMeterTier electricityMeterTier) {
		this.electricityMeterTier = electricityMeterTier;
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

	public void onNeighborChange() {
		if (!worldObj.isRemote) {
			boolean power = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

			if (isPowered != power) {
				isPowered = power;

				PacketHandler.sendToReceivers(new PacketTileEntity(this), this);
			}
		}
	}
}
