package org.halvors.electrometrics.common.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IOwnable {
    boolean hasOwner();

    boolean isOwner(EntityPlayerMP player);

    EntityPlayerMP getOwner();

    void setOwner(EntityPlayerMP player);
}
