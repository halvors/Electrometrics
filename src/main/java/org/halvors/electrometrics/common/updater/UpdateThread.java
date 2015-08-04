package org.halvors.electrometrics.common.updater;

import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.Reference;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateThread extends Thread {
    private final String releaseUrl;

    private ModVersion newModVersion;
    private boolean isNewVersionAvailable = false;
    private boolean isCriticalUpdate = false;
    private boolean isCheckedComplete = false;

    public UpdateThread(String releaseUrl) {
        super(Reference.NAME + " updater");

        this.releaseUrl = releaseUrl;
    }

    @Override
    public void run() {
        try {
            URL versionFile = new URL(releaseUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(versionFile.openStream()));
            newModVersion = ModVersion.parse(Reference.NAME, reader.readLine());
            ModVersion criticalModVersion = ModVersion.parse(Reference.NAME, reader.readLine());
            reader.close();

            ModVersion ourModVersion = ModVersion.parse(Reference.NAME, MinecraftForge.MC_VERSION + "-" + Reference.VERSION);
            isNewVersionAvailable = ourModVersion.compareTo(newModVersion) < 0;

            if (isNewVersionAvailable) {
                Electrometrics.getLogger().info("An updated version is available: " + newModVersion + ".");

                if (ourModVersion.getMinecraftVersion().compareTo(newModVersion.getMinecraftVersion()) < 0) {
                    ReleaseVersion newReleaseVersion = newModVersion.getMinecraftVersion(), ourReleaseVersion = ourModVersion.getMinecraftVersion();
                    isNewVersionAvailable = newReleaseVersion.getMajor() == ourReleaseVersion.getMajor() && newReleaseVersion.getMinor() == ourReleaseVersion.getMinor();
                }

                if (criticalModVersion != null && ourModVersion.compareTo(criticalModVersion) >= 0) {
                    isCriticalUpdate = Boolean.parseBoolean(criticalModVersion.getDescription());
                    isCriticalUpdate &= isNewVersionAvailable;
                }
            }

            if (isCriticalUpdate) {
                Electrometrics.getLogger().info("This update has been marked as CRITICAL and will ignore notification suppression.");
            }
        } catch (Exception e) {
            Electrometrics.getLogger().log(Level.WARN, AbstractLogger.CATCHING_MARKER, "Update check for " + Reference.NAME + " failed.", e);
        }

        isCheckedComplete = true;
    }

    public boolean isCheckCompleted() {
        return isCheckedComplete;
    }

    public boolean isCriticalUpdate() {
        return isCriticalUpdate;
    }

    public boolean isNewVersionAvailable() {
        return isNewVersionAvailable;
    }

    public ModVersion getNewModVersion() {
        return newModVersion;
    }
}