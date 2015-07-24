package org.halvors.electrometrics.common.tile.machine;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.RedstoneControlType;
import org.halvors.electrometrics.common.base.Tier;
import org.halvors.electrometrics.common.base.tile.ITileActiveState;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileOwnable;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;
import org.halvors.electrometrics.common.tile.TileEntityElectricityProvider;
import org.halvors.electrometrics.common.util.PlayerUtils;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * This is the TileEntity of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class TileEntityElectricityMeter extends TileEntityElectricityProvider implements ITileNetworkable, ITileActiveState, ITileOwnable, ITileRedstoneControl {
	// Whether or not this TileEntity's block is in it's active state.
	private boolean isActive;

	// The client's current active state.
	@SideOnly(Side.CLIENT)
	private boolean clientIsActive;

	// The UUID of the player owning this.
	private UUID ownerUUID;

	// The name of the player owning this.
	private String ownerName;

	// The current RedstoneControlType of this TileEntity.
	private RedstoneControlType redstoneControlType = RedstoneControlType.DISABLED;

	// The tier of this TileEntity.
	private Tier.ElectricityMeter tier;

	// The amount of energy that has passed thru.
	private double electricityCount;

	public TileEntityElectricityMeter() {
		this(MachineType.BASIC_ELECTRICITY_METER, Tier.ElectricityMeter.BASIC);
	}

	public TileEntityElectricityMeter(MachineType machineType, Tier.ElectricityMeter tier) {
		super(machineType, tier.getMaxEnergy(), tier.getMaxTransfer());

		this.tier = tier;
	}

	@Override
	public void validate() {
		super.validate();

		PacketHandler.sendToServer(new PacketRequestData(this));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		isActive = nbtTagCompound.getBoolean("isActive");

		if (nbtTagCompound.hasKey("ownerUUIDM") && nbtTagCompound.hasKey("ownerUUIDL")) {
			ownerUUID = new UUID(nbtTagCompound.getLong("ownerUUIDM"), nbtTagCompound.getLong("ownerUUIDL"));
		}

		if (nbtTagCompound.hasKey("ownerName")) {
			ownerName = nbtTagCompound.getString("ownerName");
		}

		redstoneControlType = RedstoneControlType.values()[nbtTagCompound.getInteger("redstoneControlType")];
		tier = Tier.ElectricityMeter.values()[nbtTagCompound.getInteger("tier")];
		electricityCount = nbtTagCompound.getDouble("electricityCount");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setBoolean("isActive", isActive);

		if (ownerUUID != null) {
			nbtTagCompound.setLong("ownerUUIDM", ownerUUID.getMostSignificantBits());
			nbtTagCompound.setLong("ownerUUIDL", ownerUUID.getLeastSignificantBits());
		}

		if (ownerName != null) {
			nbtTagCompound.setString("ownerName", ownerName);
		}

		nbtTagCompound.setInteger("redstoneControlType", redstoneControlType.ordinal());
		nbtTagCompound.setInteger("tier", tier.ordinal());
		nbtTagCompound.setDouble("electricityCount", electricityCount);
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		isActive = dataStream.readBoolean();

		long ownerUUIDMostSignificantBits = dataStream.readLong();
		long ownerUUIDLeastSignificantBits = dataStream.readLong();

		if (ownerUUIDMostSignificantBits != 0 && ownerUUIDLeastSignificantBits != 0) {
			ownerUUID = new UUID(ownerUUIDMostSignificantBits, ownerUUIDLeastSignificantBits);
		}

		String ownerNameText = ByteBufUtils.readUTF8String(dataStream);

		if (!ownerNameText.isEmpty()) {
			ownerName = ownerNameText;
		}

		redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];

		// Check if client is in sync with the server, if not update it.
		if (worldObj.isRemote && clientIsActive != isActive) {
			clientIsActive = isActive;

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		tier = Tier.ElectricityMeter.values()[dataStream.readInt()];
		electricityCount = dataStream.readDouble();
	}

	@Override
	public List<Object> getPacketData(List<Object> objects) {
		super.getPacketData(objects);

		objects.add(isActive);
		objects.add(ownerUUID != null ? ownerUUID.getMostSignificantBits() : 0);
		objects.add(ownerUUID != null ? ownerUUID.getLeastSignificantBits() : 0);
		objects.add(ownerName != null ? ownerName : "");
		objects.add(redstoneControlType.ordinal());
		objects.add(tier.ordinal());
		objects.add(electricityCount);

		return objects;
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
	protected EnumSet<ForgeDirection> getExtractingSides() {
		return EnumSet.of(ForgeDirection.getOrientation(facing).getRotation(ForgeDirection.DOWN));
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean hasOwner() {
		return ownerUUID != null && ownerName != null;
	}

	@Override
	public boolean isOwner(EntityPlayer player) {
		return hasOwner() && ownerUUID.equals(player.getPersistentID());
	}

	@Override
	public EntityPlayer getOwner() {
		return PlayerUtils.getPlayerFromUUID(ownerUUID);
	}

	@Override
	public String getOwnerName() {
		return ownerName;
	}

	@Override
	public void setOwner(EntityPlayer player) {
		this.ownerUUID = player.getPersistentID();
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

	public Tier.ElectricityMeter getTier() {
		return tier;
	}

	public void setTier(Tier.ElectricityMeter electricityMeterTier) {
		this.tier = electricityMeterTier;
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
}
