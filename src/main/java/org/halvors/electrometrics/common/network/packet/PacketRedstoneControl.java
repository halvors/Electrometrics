package org.halvors.electrometrics.common.network.packet;


import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.common.base.RedstoneControlType;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.tile.TileEntity;

public class PacketRedstoneControl extends PacketLocation implements IMessage {
    private RedstoneControlType redstoneControlType;

    public PacketRedstoneControl() {

    }

    public PacketRedstoneControl(TileEntity tileEntity, RedstoneControlType redstoneControlType) {
        super(tileEntity);

        this.redstoneControlType = redstoneControlType;
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(redstoneControlType.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];
    }

    public static class PacketRedstoneControlMessage implements IMessageHandler<PacketRedstoneControl, IMessage> {
        @Override
        public IMessage onMessage(PacketRedstoneControl message, MessageContext context) {
            EntityPlayer player = NetworkHandler.getPlayer(context);
            TileEntity tileEntity = message.getLocation().getTileEntity(player.worldObj);

            if (tileEntity instanceof ITileRedstoneControl) {
                ((ITileRedstoneControl) tileEntity).setControlType(message.redstoneControlType);
            }

            return null;
        }
    }
}

