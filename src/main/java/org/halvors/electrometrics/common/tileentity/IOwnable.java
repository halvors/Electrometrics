package org.halvors.electrometrics.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IOwnable {
	boolean isOwner(EntityPlayer player);

	EntityPlayerMP getOwner();

	void setOwner(EntityPlayer player);
}
