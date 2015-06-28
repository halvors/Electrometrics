package org.halvors.electrometrics.common.tileentity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.network.PacketRequestData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This is the TileEntity of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
public class TileEntityElectricityMeter extends TileEntityEnergyProvider implements INetworkable, IActiveState, IOwnable {
	// The amount of energy that has passed thru.
	private double electricityCount;

	// Whether or not this block is in it's active state.
	private boolean isActive;

	// The client's current active state.
	@SideOnly(Side.CLIENT)
	public boolean clientIsActive;

	// The UUID of the player owning this.
	public UUID owner;

	public TileEntityElectricityMeter() {
		super(25600, 25600, 25600);
	}

	@Override
	public void validate() {
		super.validate();

		if (worldObj.isRemote) {
			PacketHandler.getNetwork().sendToServer(new PacketRequestData(this));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags) {
		super.readFromNBT(nbtTags);

		isActive = nbtTags.getBoolean("isActive");
		owner = UUID.fromString(nbtTags.getString("owner"));
		electricityCount = nbtTags.getDouble("electricityCount");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags) {
		super.writeToNBT(nbtTags);

		nbtTags.setBoolean("isActive", isActive);
		nbtTags.setString("owner", owner.toString());
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

		isActive = dataStream.readBoolean();
		owner = UUID.fromString(ByteBufUtils.readUTF8String(dataStream));
		electricityCount = dataStream.readDouble();

		// Check if client is in sync with the server, if not update it.
		if (clientIsActive != isActive) {
			clientIsActive = isActive;

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public ArrayList<Object> getPacketData(ArrayList<Object> data) {
		super.getPacketData(data);

		data.add(isActive);
		data.add(owner.toString());
		data.add(electricityCount);

		return data;
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
	public boolean isOwner(EntityPlayer player) {
		return owner.equals(player.getUniqueID());
	}

	@Override
	public EntityPlayer getOwner() {
		List<WorldServer> worldList = Arrays.asList(Minecraft.getMinecraft().getIntegratedServer().worldServers);

		for (World world : worldList) {
			List<EntityPlayer> playerList = world.playerEntities;

			for (EntityPlayer player : playerList) {
				if (isOwner(player)) {
					return player;
				}
			}
		}

		return null;
	}

	@Override
	public void setOwner(EntityPlayer player) {
		this.owner = player.getUniqueID();
	}

	/**
	 * Returns the amount of energy that this block has totally received.
	 */
	public double getElectricityCount() {
		return electricityCount;
	}

	/*
	 * Sets the amount of energy that this block has totally received.
	 */
	public void setElectricityCount(double electricityCount) {
		this.electricityCount = electricityCount;
	}
}