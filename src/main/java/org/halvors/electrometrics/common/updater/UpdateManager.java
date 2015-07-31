package org.halvors.electrometrics.common.updater;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import javafx.scene.paint.Color;
import joptsimple.internal.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import org.halvors.electrometrics.common.ConfigurationManager.General;

public class UpdateManager {
    private static transient int pollOffset = 0;
    private static final ChatStyle description = new ChatStyle();
    private static final ChatStyle version = new ChatStyle();
    private static final ChatStyle modName = new ChatStyle();
    private static final ChatStyle download = new ChatStyle();
    private static final ChatStyle white = new ChatStyle();

    static {
        description.setColor(EnumChatFormatting.GRAY);
        version.setColor(EnumChatFormatting.AQUA);
        modName.setColor(EnumChatFormatting.GOLD);
        download.setColor(EnumChatFormatting.GREEN);
        white.setColor(EnumChatFormatting.WHITE);

        ChatStyle tooltip = new ChatStyle();
        tooltip.setColor(EnumChatFormatting.YELLOW);
        IChatComponent msg = new ChatComponentTranslation("info.cofh.updater.tooltip").setChatStyle(tooltip);
        download.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg));
    }

    public static void registerUpdater(UpdateManager manager) {
        FMLCommonHandler.instance().bus().register(manager);
    }

    private boolean notificationDisplayed;
    private final IUpdatableMod mod;
    private final UpdateCheckThread updateThread;
    private int lastPoll = 400;

    public UpdateManager(IUpdatableMod mod, String releaseUrl) {
        this.mod = mod;
        this.updateThread = new UpdateCheckThread(mod, releaseUrl);

        lastPoll += (pollOffset += 140);
        updateThread.start();
    }

    @SubscribeEvent
    public void tickStart(TickEvent.PlayerTickEvent evt) {
        if (evt.phase != TickEvent.Phase.START) {
            return;
        }

        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
            //if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(evt.player.getGameProfile())) { // func_152596_g
                return;
            //}
        }

        if (lastPoll > 0) {
            --lastPoll;
            return;
        }

        lastPoll = 400;

        if (!notificationDisplayed && updateThread.isCheckComplete()) {
            notificationDisplayed = true;
            FMLCommonHandler.instance().bus().unregister(this);

            if (updateThread.isNewVersionAvailable()) {
                if (!General.enableUpdateNotice && !updateThread.isCriticalUpdate()) {
                    return;
                }

                ModVersion newVersion = updateThread.getNewVersion();
                EntityPlayer player = evt.player;
                IChatComponent chat = new ChatComponentText("");
                ChatStyle data = modName.createShallowCopy();
                IChatComponent msg = new ChatComponentText(newVersion.getModVersion().toString()).setChatStyle(version);
                data.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg));
                chat.appendSibling(new ChatComponentText("[" + mod.getModName() + "] ").setChatStyle(data));
                chat.appendSibling(new ChatComponentTranslation("info.cofh.updater.version").setChatStyle(white));
                chat.appendText(Color.GOLD + ":");
                player.addChatMessage(chat);

                chat = new ChatComponentText(newVersion.getDescription()).setChatStyle(description);
                player.addChatMessage(chat);
            }
        }
    }

}
