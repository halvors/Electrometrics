package org.halvors.electrometrics.common.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IOwnable {
    boolean hasOwner();

    EntityPlayerMP getOwner();

    void setOwner(EntityPlayerMP player);
}
