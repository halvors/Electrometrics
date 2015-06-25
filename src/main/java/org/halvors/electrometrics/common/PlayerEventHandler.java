package org.halvors.electrometrics.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.network.PacketConfigurationSync;
import org.halvors.electrometrics.common.network.PacketHandler;

public class PlayerEventHandler {
    public PlayerEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLoginEvent(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        World world = player.worldObj;

        if (!world.isRemote) {
            PacketHandler.getNetwork().sendTo(new PacketConfigurationSync(), (EntityPlayerMP) event.player);

            Electrometrics.getLogger().info("Sent configuration to '" + player.getDisplayName() + "'.");
        }
    }
}
