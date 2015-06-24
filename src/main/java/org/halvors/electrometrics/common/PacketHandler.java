package org.halvors.electrometrics.common;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.electrometrics.Reference;
import org.halvors.electrometrics.common.network.ElectricityMeterMessage;

public class PacketHandler {
    private static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

    public void initialize() {
        network.registerMessage(ElectricityMeterMessage.Handler.class, ElectricityMeterMessage.class, 0, Side.CLIENT);
        network.registerMessage(ElectricityMeterMessage.Handler.class, ElectricityMeterMessage.class, 0, Side.SERVER);
    }

    public static EntityPlayer getPlayer(MessageContext context) {
        return context.side.isClient() ? Minecraft.getMinecraft().thePlayer : context.getServerHandler().playerEntity;
    }

    public static SimpleNetworkWrapper getNetwork() {
        return network;
    }
}
