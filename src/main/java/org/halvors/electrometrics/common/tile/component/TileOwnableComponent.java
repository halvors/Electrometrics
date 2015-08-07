package org.halvors.electrometrics.common.tile.component;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileOwnable;
import org.halvors.electrometrics.common.component.IComponentContainer;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.PlayerUtils;

import java.util.List;
import java.util.UUID;

public class TileOwnableComponent extends TileComponentBase implements ITileComponent, ITileNetworkable, ITileOwnable {
    // The UUID of the player owning this.
    private UUID ownerUUID;

    // The name of the player owning this.
    private String ownerName;

    public <T extends TileEntity & IComponentContainer> TileOwnableComponent(T componentContainer) {
        super(componentContainer);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey("ownerUUIDM") && nbtTagCompound.hasKey("ownerUUIDL")) {
            ownerUUID = new UUID(nbtTagCompound.getLong("ownerUUIDM"), nbtTagCompound.getLong("ownerUUIDL"));
        }

        if (nbtTagCompound.hasKey("ownerName")) {
            ownerName = nbtTagCompound.getString("ownerName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        if (ownerUUID != null) {
            nbtTagCompound.setLong("ownerUUIDM", ownerUUID.getMostSignificantBits());
            nbtTagCompound.setLong("ownerUUIDL", ownerUUID.getLeastSignificantBits());
        }

        if (ownerName != null) {
            nbtTagCompound.setString("ownerName", ownerName);
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onNeighborChange() {

    }

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        long ownerUUIDMostSignificantBits = dataStream.readLong();
        long ownerUUIDLeastSignificantBits = dataStream.readLong();

        if (ownerUUIDMostSignificantBits != 0 && ownerUUIDLeastSignificantBits != 0) {
            ownerUUID = new UUID(ownerUUIDMostSignificantBits, ownerUUIDLeastSignificantBits);
        }

        String ownerName = ByteBufUtils.readUTF8String(dataStream);

        if (!ownerName.isEmpty()) {
            this.ownerName = ownerName;
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> list) {
        list.add(ownerUUID != null ? ownerUUID.getMostSignificantBits() : 0);
        list.add(ownerUUID != null ? ownerUUID.getLeastSignificantBits() : 0);
        list.add(ownerName != null ? ownerName : "");

        return list;
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
}