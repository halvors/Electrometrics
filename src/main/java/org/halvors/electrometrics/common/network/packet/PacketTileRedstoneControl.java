package org.halvors.electrometrics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import org.halvors.electrometrics.common.base.RedstoneControlType;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.component.ITileComponent;

import java.util.ArrayList;
import java.util.List;

public class PacketTileRedstoneControl<TE extends TileEntity & ITileNetworkable, TC extends ITileComponent & ITileNetworkable> extends PacketLocation implements IMessage {
    private PacketType packetType;
    private RedstoneControlType redstoneControlType;

    public PacketTileRedstoneControl() {

    }

    public PacketTileRedstoneControl(TE tileEntity, PacketType packetType, RedstoneControlType redstoneControlType) {
        super(tileEntity);

        this.packetType = packetType;

        switch (packetType) {
            case UPDATE:
            case RESPONSE:
                this.redstoneControlType = redstoneControlType;
                break;
        }
    }


    /*
    public <TE extends PacketTileRedstoneControl<TE extends ITileRedstoneControl>> PacketTileRedstoneControl(TER tileEntity, PacketType packetType) {
        this(tileEntity, packetType, tileEntity.getControlType());
    }
    */

    public PacketTileRedstoneControl(TC tileComponent, PacketType packetType, RedstoneControlType redstoneControlType) {
        this((TE) tileComponent.getTileEntity(), packetType, redstoneControlType);
    }

    /*
    public <T extends TE & ITileRedstoneControl> PacketTileRedstoneControl(T tileComponent, PacketType packetType) {
        this(tileComponent, packetType, tileComponent.getControlType());
    }
    */

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        packetType = PacketType.values()[dataStream.readInt()];

        switch (packetType) {
            case UPDATE:
            case RESPONSE:
                redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];
                break;
        }
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        List<Object> objects = new ArrayList<>();
        objects.add(packetType.ordinal());

        switch (packetType) {
            case UPDATE:
            case RESPONSE:
                objects.add(redstoneControlType.ordinal());
                break;
        }

        NetworkHandler.writeObjects(objects, dataStream);
    }

    public static class PacketTileRedstoneControlMessage implements IMessageHandler<PacketTileRedstoneControl, IMessage> {
        @Override
        public IMessage onMessage(PacketTileRedstoneControl message, MessageContext messageContext) {
            return onPacketTileRedstoneControlMessage(message, messageContext);
        }

        @SuppressWarnings("unchecked")
        public <TE extends TileEntity & ITileNetworkable & ITileRedstoneControl> IMessage onPacketTileRedstoneControlMessage(PacketTileRedstoneControl message, MessageContext messageContext) {
            World world = NetworkHandler.getWorld(messageContext);
            TileEntity tileEntity = message.getLocation().getTileEntity(world);

            if (tileEntity != null && tileEntity instanceof ITileRedstoneControl) {
                TE tileRedstoneControl = (TE) tileEntity;

                switch (message.packetType) {
                    case UPDATE:
                        if (messageContext.side.isServer()) {
                            tileRedstoneControl.setControlType(message.redstoneControlType);

                            return new PacketTileRedstoneControl(tileRedstoneControl, PacketType.RESPONSE, tileRedstoneControl.getControlType());
                        }
                        break;

                    case RESPONSE:
                        if (messageContext.side.isClient()) {
                            tileRedstoneControl.setControlType(message.redstoneControlType);
                        }
                        break;
                }
            }

            return null;
        }
    }

    public enum PacketType {
        UPDATE,
        RESPONSE
    }
}

