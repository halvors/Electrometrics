package org.halvors.electrometrics.common.updater;

import com.google.common.base.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.halvors.electrometrics.common.ConfigurationManager.General;

import static net.minecraft.util.EnumChatFormatting.*;

public class UpdateManager {
    private static transient int pollOffset = 0;
    private static final ChatStyle description = new ChatStyle();
    private static final ChatStyle version = new ChatStyle();
    private static final ChatStyle modname = new ChatStyle();
    private static final ChatStyle download = new ChatStyle();
    private static final ChatStyle white = new ChatStyle();

    static {
        description.setColor(GRAY);
        version.setColor(AQUA);
        modname.setColor(GOLD);
        download.setColor(GREEN);
        white.setColor(WHITE);
        {
            ChatStyle tooltip = new ChatStyle();
            tooltip.setColor(YELLOW);
            IChatComponent msg = new ChatComponentTranslation("tooltip.clickToDownload").setChatStyle(tooltip);
            download.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg));
        }
    }

    public static void registerUpdater(UpdateManager manager) {
        FMLCommonHandler.instance().bus().register(manager);
    }

    private boolean _notificationDisplayed;
    private final IUpdatableMod _mod;
    private final UpdateThread updateThread;
    private final String _downloadUrl;
    private int lastPoll = 400;

    public UpdateManager(IUpdatableMod mod, String releaseUrl, String downloadUrl) {

        _mod = mod;
        updateThread = new UpdateThread(mod, releaseUrl, downloadUrl);
        updateThread.start();
        _downloadUrl = downloadUrl;
        lastPoll += (pollOffset += 140);
    }

    @SubscribeEvent
    public void tickStart(PlayerTickEvent event) {
        if (event.phase != Phase.START) {
            return;
        }

        /*
        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
            if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(evt.player.getGameProfile())) {
                return;
            }
        }
        */

        if (lastPoll > 0) {
            lastPoll--;
        } else {
            lastPoll = 400;

            if (!_notificationDisplayed && updateThread.isCheckCompleted()) {
                _notificationDisplayed = true;
                FMLCommonHandler.instance().bus().unregister(this);

                if (updateThread.isNewVersionAvailable()) {
                    if (General.enableUpdateNotice && updateThread.isCriticalUpdate()) {
                        ModVersion newVersion = updateThread.getNewVersion();

                        EntityPlayer player = event.player;
                        IChatComponent chat = new ChatComponentText("");
                        {
                            ChatStyle data = modname.createShallowCopy();
                            IChatComponent msg = new ChatComponentText(newVersion.getModVersion().toString()).setChatStyle(version);
                            data.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg));
                            chat.appendSibling(new ChatComponentText("[" + _mod.getModName() + "] ").setChatStyle(data));
                        }

                        chat.appendSibling(new ChatComponentTranslation("tooltip.versionAvailable").setChatStyle(white));
                        chat.appendText(GOLD + ":");
                        player.addChatMessage(chat);
                        chat = new ChatComponentText("");

                        if (!Strings.isNullOrEmpty(_downloadUrl)) {
                            chat.appendText(WHITE + "[");
                            ChatStyle data = download.createShallowCopy();
                            data.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, _downloadUrl));
                            chat.appendSibling(new ChatComponentTranslation("tooltip.download").setChatStyle(data));
                            chat.appendText(WHITE + "] ");
                        }

                        chat.appendSibling(new ChatComponentText(newVersion.getDescription()).setChatStyle(description));
                        player.addChatMessage(chat);
                    }
                }
            }
        }
    }
}
