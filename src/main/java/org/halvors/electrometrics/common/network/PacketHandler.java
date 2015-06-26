package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.Reference;

/**
 * This is the PacketHandler which is responsible for registering the packet that we are going to use.
 *
 * @author halvors
 */
public class PacketHandler {
    private static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

    public void initialize() {
        // Client side
        network.registerMessage(PacketConfigurationSync.class, PacketConfigurationSync.class, 0, Side.CLIENT);
        network.registerMessage(PacketElectricityMeter.class, PacketElectricityMeter.class, 1, Side.CLIENT);

        // Server side
        network.registerMessage(PacketElectricityMeter.class, PacketElectricityMeter.class, 1, Side.SERVER);
    }

    public static EntityPlayer getPlayer(MessageContext messageContext) {
        return messageContext.side.isClient() ? Minecraft.getMinecraft().thePlayer : messageContext.getServerHandler().playerEntity;
    }

    public static SimpleNetworkWrapper getNetwork() {
        return network;
    }
}
