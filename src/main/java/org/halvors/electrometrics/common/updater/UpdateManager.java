package org.halvors.electrometrics.common.updater;

import com.google.common.base.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;
import org.halvors.electrometrics.common.ConfigurationManager.General;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.util.render.Color;

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

    private final UpdateThread updateThread;
    private final String downloadUrl;
    private boolean notificationDisplayed;
    private int lastPoll = 400;

    public UpdateManager(String releaseUrl, String downloadUrl) {
        this.updateThread = new UpdateThread(releaseUrl);
        this.downloadUrl = downloadUrl;

        updateThread.start();
        lastPoll += (pollOffset += 140);
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if (event.phase == Phase.END) {
            if (lastPoll > 0) {
                lastPoll--;
            } else {
                lastPoll = 400;

                if (!notificationDisplayed && updateThread.isCheckCompleted()) {
                    notificationDisplayed = true;
                    FMLCommonHandler.instance().bus().unregister(this);

                    if (updateThread.isNewVersionAvailable()) {
                        if (General.enableUpdateNotice && updateThread.isCriticalUpdate()) {
                            ModVersion newModVersion = updateThread.getNewModVersion();
                            EntityPlayer player = event.player;



                            // Display notification message.
                            ChatStyle modnameData = modname.createShallowCopy();
                            modnameData.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(newModVersion.getModVersion().toString()).setChatStyle(version)));

                            IChatComponent notificationChatMessage = new ChatComponentText("");
                            notificationChatMessage.appendSibling(new ChatComponentText("[" + Reference.NAME + "] ").setChatStyle(modnameData));
                            notificationChatMessage.appendSibling(new ChatComponentTranslation("tooltip.versionAvailable").setChatStyle(white));
                            notificationChatMessage.appendText(Color.YELLOW + ":");

                            // Display description.
                            IChatComponent descriptionChatMessage = new ChatComponentText("");

                            if (!Strings.isNullOrEmpty(downloadUrl)) {
                                descriptionChatMessage.appendText(Color.WHITE + "[");

                                ChatStyle downloadData = download.createShallowCopy();
                                downloadData.setChatClickEvent(new ClickEvent(Action.OPEN_URL, downloadUrl));

                                descriptionChatMessage.appendSibling(new ChatComponentTranslation("tooltip.download").setChatStyle(downloadData));
                                descriptionChatMessage.appendText(Color.WHITE + "] ");
                            }

                            descriptionChatMessage.appendSibling(new ChatComponentText(newModVersion.getDescription()).setChatStyle(description));

                            // Send the chat messages to the player.
                            player.addChatMessage(descriptionChatMessage);
                            player.addChatMessage(notificationChatMessage);
                        }
                    }
                }
            }
        }
    }
}