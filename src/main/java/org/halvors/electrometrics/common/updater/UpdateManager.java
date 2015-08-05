package org.halvors.electrometrics.common.updater;

import com.google.common.base.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;
import org.halvors.electrometrics.common.ConfigurationManager.General;

public class UpdateManager {

    private static transient int pollOffset = 0;
    private static final ChatStyle description = new ChatStyle();
    private static final ChatStyle version = new ChatStyle();
    private static final ChatStyle modname = new ChatStyle();
    private static final ChatStyle download = new ChatStyle();
    private static final ChatStyle white = new ChatStyle();

    static {
        description.setColor(EnumChatFormatting.GRAY);
        version.setColor(EnumChatFormatting.AQUA);
        modname.setColor(EnumChatFormatting.GOLD);
        download.setColor(EnumChatFormatting.GREEN);
        white.setColor(EnumChatFormatting.WHITE);

        ChatStyle tooltip = new ChatStyle();
        tooltip.setColor(EnumChatFormatting.YELLOW);
        IChatComponent message = new ChatComponentTranslation("tooltip.clickToDownload").setChatStyle(tooltip);
        download.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, message));
    }

    public static void registerUpdater(UpdateManager manager) {
        FMLCommonHandler.instance().bus().register(manager);
    }

    private final IUpdatableMod mod;
    private final UpdateThread updateThread;
    private final String downloadUrl;

    private boolean isNotificationDisplayed;
    private int lastPoll = 400;

    public UpdateManager(IUpdatableMod mod, String releaseUrl, String downloadUrl) {
        this.mod = mod;
        this.updateThread = new UpdateThread(mod, releaseUrl, downloadUrl);
        this.downloadUrl = downloadUrl;

        updateThread.start();
        lastPoll += (pollOffset += 140);
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if (event.phase == Phase.END) {
            if (lastPoll > 0) {
                --lastPoll;
            } else {
                lastPoll = 400;

                if (!isNotificationDisplayed && updateThread.isCheckCompleted()) {
                    isNotificationDisplayed = true;
                    FMLCommonHandler.instance().bus().unregister(this);

                    if (updateThread.isNewVersionAvailable()) {
                        if (!General.enableUpdateNotice && !updateThread.isCriticalUpdate()) {
                            return;
                        }

                        ModVersion newVersion = updateThread.getNewVersion();
                        EntityPlayer player = event.player;

                        IChatComponent chat = new ChatComponentText("");
                        {
                            ChatStyle data = modname.createShallowCopy();
                            IChatComponent msg = new ChatComponentText(newVersion.getModVersion().toString()).setChatStyle(version);
                            data.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg));
                            chat.appendSibling(new ChatComponentText("[" + mod.getModName() + "] ").setChatStyle(data));
                        }

                        chat.appendSibling(new ChatComponentTranslation("tooltip.newVersionAvailable").setChatStyle(white));
                        chat.appendText(EnumChatFormatting.GOLD + ":");
                        player.addChatMessage(chat);
                        chat = new ChatComponentText("");

                        if (!Strings.isNullOrEmpty(downloadUrl)) {
                            chat.appendText(EnumChatFormatting.WHITE + "[");
                            ChatStyle data = download.createShallowCopy();
                            data.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));
                            chat.appendSibling(new ChatComponentTranslation("info.cofh.updater.download").setChatStyle(data));
                            chat.appendText(EnumChatFormatting.WHITE + "] ");
                        }

                        chat.appendSibling(new ChatComponentText(newVersion.getDescription()).setChatStyle(description));
                        player.addChatMessage(chat);
                    }
                }
            }
        }
    }
}
