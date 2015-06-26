package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.halvors.electrometrics.Reference;

/**
 * This is the PacketHandler which is responsible for registering the packet that we are going to use.
 *
 * @author halvors
 */
public class PacketHandler {
    private static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

    public void initialize() {

        network.registerMessage(PacketConfigurationSync.class, PacketConfigurationSync.class, 0, Side.CLIENT);
        network.registerMessage(PacketRequestData.class, PacketRequestData.class, 1, Side.SERVER);
        network.registerMessage(PacketTileEntity.class, PacketTileEntity.class, 2, Side.CLIENT);
        network.registerMessage(PacketTileEntity.class, PacketTileEntity.class, 2, Side.SERVER);
    }

    public static EntityPlayerMP getPlayer(MessageContext context) {
        EntityPlayer player = context.side.isClient() ? Minecraft.getMinecraft().thePlayer : context.getServerHandler().playerEntity;

        return (EntityPlayerMP) player;
    }

    public static SimpleNetworkWrapper getNetwork() {
        return network;
    }
}
