package org.halvors.electrometrics.common.updater;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.AbstractLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateCheckThread extends Thread {
    private final IUpdatableMod mod;
    private final String releaseUrl;

    private boolean checkComplete, newVersionAvailable, criticalUpdate;
    private ModVersion newVersion;

    public UpdateCheckThread(IUpdatableMod mod, String releaseUrl) {
        super("Updater:" + mod.getModId());

        this.mod = mod;
        this.releaseUrl = releaseUrl;
    }

    @Override
    public void run() {
        try {
            String id = mod.getModName();
            ModVersion ourVer = ModVersion.parse(id, "1.7.10-" + mod.getModVersion());

            URL versionFile = new URL(releaseUrl + "VERSION");
            BufferedReader reader = new BufferedReader(new InputStreamReader(versionFile.openStream()));
            ModVersion newVer = new ModVersion(id, reader.readLine()); //ModVersion.parse(id, reader.readLine());
            //ModVersion critVer = new ModVersion(id, reader.readLine()); //ModVersion.parse(id, reader.readLine());
            reader.close();

            /*
            if (newVer == null) {
                break l;
            }
            */

            newVersion = newVer;
            newVersionAvailable = ourVer.compareTo(newVer) < 0;

            if (newVersionAvailable) {
                mod.getLogger().info("An updated version of " + mod.getModName() + " is available: " + newVer + ".");

                if (ourVer.getMinecraftVersion().compareTo(newVer.getMinecraftVersion()) < 0) {
                    ReleaseVersion newv = newVer.getMinecraftVersion(), our = ourVer.getMinecraftVersion();
                    newVersionAvailable = newv.getMajor() == our.getMajor() && newv.getMinor() == our.getMinor();
                }

                /*
                if (critVer != null && ourVer.compareTo(critVer) >= 0) {
                    criticalUpdate = Boolean.parseBoolean(critVer.getDescription());
                    criticalUpdate &= newVersionAvailable;
                }
                */
            }

            if (criticalUpdate) {
                mod.getLogger().info("This update has been marked as CRITICAL and will ignore notification suppression.");
            }
        } catch (Exception e) {
            mod.getLogger().log(Level.WARN, AbstractLogger.CATCHING_MARKER, "Update check for " + mod.getModName() + " failed.", e);
        }

        checkComplete = true;
    }

    public boolean isCheckComplete() {
        return checkComplete;
    }

    public boolean isCriticalUpdate() {
        return criticalUpdate;
    }

    public boolean isNewVersionAvailable() {
        return newVersionAvailable;
    }

    public ModVersion getNewVersion() {
        return newVersion;
    }
}
