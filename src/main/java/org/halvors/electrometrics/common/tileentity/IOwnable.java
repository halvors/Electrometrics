package org.halvors.electrometrics.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;

public interface IOwnable {
    boolean hasOwner();

    boolean isOwner(EntityPlayer player);

    EntityPlayer getOwner();

    void setOwner(EntityPlayer player);
}