package org.halvors.electrometrics.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.Reference;

public class PacketHandler {
    private static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

    public void initialize() {
        // Client side
        network.registerMessage(PacketTileEntityElectricityMeter.class, PacketTileEntityElectricityMeter.class, 0, Side.CLIENT);

        // Server side
        network.registerMessage(PacketTileEntityElectricityMeter.class, PacketTileEntityElectricityMeter.class, 0, Side.SERVER);
    }

    public static EntityPlayer getPlayer(MessageContext context) {
        return context.side.isClient() ? Minecraft.getMinecraft().thePlayer : context.getServerHandler().playerEntity;
    }

    public static SimpleNetworkWrapper getNetwork() {
        return network;
    }
}
