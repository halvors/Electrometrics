package org.halvors.electrometrics.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IOwnable {
	/**
	 * Check if this ownable has an owner.
	 * @return true if this ownable has an owner.
	 */
	boolean hasOwner();

	/**
	 * Check if the player is owning this.
	 * @param player the player to check if is the owner of this.
	 * @return true if the player is the owner of this.
	 */
	boolean isOwner(EntityPlayer player);

	/**
	 * Get the player owning this.
	 * @return player
	 */
	EntityPlayerMP getOwner();

	/**
	 * Get the name of the player owning this.
	 * @return name
	 */
	String getOwnerName();

	/**
	 * Sets the player owning this.
	 * @param player the player going to be set as owner of this.
	 */
	void setOwner(EntityPlayer player);
}
