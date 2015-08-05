package org.halvors.electrometrics.common.updater;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.halvors.electrometrics.Electrometrics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateThread extends Thread {

    private final String releaseUrl, downloadUrl;
    private final IUpdatableMod mod;

    private ModVersion newModVersion;
    private boolean isCheckCompleted;
    private boolean isNewVersionAvailable;
    private boolean isCriticalUpdate;

    public UpdateThread(IUpdatableMod mod, String releaseUrl, String downloadUrl) {
        super(mod.getModId() + " updater");

        this.mod = mod;
        this.releaseUrl = releaseUrl;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public void run() {
        l: try {
            ModVersion ourVersion = ModVersion.parse(mod.getModName(), mod.getModVersion());

            URL versionFile = new URL(releaseUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(versionFile.openStream()));
            newModVersion = ModVersion.parse(mod.getModName(), reader.readLine());
            ModVersion criticalVersion = ModVersion.parse(mod.getModName(), reader.readLine());
            reader.close();

            if (newModVersion == null) {
                break l;
            }

            isNewVersionAvailable = ourVersion.compareTo(newModVersion) < 0;

            if (isNewVersionAvailable) {
                Electrometrics.getLogger().info("An updated version of " + mod.getModName() + " is available: " + newModVersion + ".");

                if (ourVersion.getMinecraftVersion().compareTo(newModVersion.getMinecraftVersion()) < 0) {
                    ReleaseVersion newv = newModVersion.getMinecraftVersion(), our = ourVersion.getMinecraftVersion();
                    isNewVersionAvailable = newv.major() == our.major() && newv.minor() == our.minor();
                }
                if (criticalVersion != null && ourVersion.compareTo(criticalVersion) >= 0) {
                    isCriticalUpdate = Boolean.parseBoolean(criticalVersion.getDescription());
                    isCriticalUpdate &= isNewVersionAvailable;
                }
            }
            if (isCriticalUpdate) {
                Electrometrics.getLogger().info("This update has been marked as CRITICAL and will ignore notification suppression.");
            }

            if (Loader.isModLoaded("VersionChecker")) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setString("modDisplayName", mod.getModName());
                compound.setString("oldVersion", ourVersion.toString());
                compound.setString("newVersion", newModVersion.toString());

                if (downloadUrl != null) {
                    compound.setString("updateUrl", downloadUrl);
                    compound.setBoolean("isDirectLink", false);
                }

                FMLInterModComms.sendRuntimeMessage(mod.getModId(), "VersionChecker", "addUpdate", compound);
                isNewVersionAvailable &= isCriticalUpdate;
            }
        } catch (Exception e) {
            Electrometrics.getLogger().log(Level.WARN, AbstractLogger.CATCHING_MARKER, "Update check for " + mod.getModName() + " failed.", e);
        }

        isCheckCompleted = true;
    }

    public boolean isCheckCompleted() {
        return isCheckCompleted;
    }

    public boolean isCriticalUpdate() {
        return isCriticalUpdate;
    }

    public boolean isNewVersionAvailable() {
        return isNewVersionAvailable;
    }

    public ModVersion getNewVersion() {
        return newModVersion;
    }
}
