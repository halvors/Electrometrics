package org.halvors.electrometrics.common.updater;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ModVersion implements ArtifactVersion {
    private final String label;
    private final ReleaseVersion _mcVer;
    private final ReleaseVersion _modVer;
    private final String description;

    public ModVersion(String label, ReleaseVersion minecraftVersion, ReleaseVersion modVersion, String description) {
        this.label = label;
        _mcVer = minecraftVersion;
        _modVer = modVersion;
        this.description = description;
    }

    public ModVersion(String label, String s) {
        String[] parts = s.split(" ", 2);
        String description = null;

        if (parts.length > 1) {
            description = parts[1].trim();
        }

        parts = parts[0].split("-", 2);

        this.label = label;
        _mcVer = new ReleaseVersion("Minecraft", parts[0]);
        _modVer = new ReleaseVersion(label, parts[1]);
        this.description = description;
    }

    public static ModVersion parse(String label, String s) {
        if (s == null || s.length() == 0) {
            return null;
        }

        return new ModVersion(label, s);
    }

    @Override
    public int compareTo(ArtifactVersion artifactVersion) {
        if (artifactVersion instanceof ModVersion) {
            return compareTo((ModVersion) artifactVersion);
        }

        if (artifactVersion instanceof ReleaseVersion) {
            ReleaseVersion releaseVersion = (ReleaseVersion) artifactVersion;

            if (label.equals(releaseVersion.getLabel())) {
                return _modVer.compareTo(releaseVersion);
            } else if ("Minecraft".equals(releaseVersion.getLabel())) {
                return _mcVer.compareTo(releaseVersion);
            }
        }

        return 0;
    }

    public int compareTo(ModVersion modVersion) {
        if (_mcVer.compareTo(modVersion.getMinecraftVersion()) != 0) {
            return _mcVer.compareTo(modVersion.getMinecraftVersion());
        }

        return _modVer.compareTo(modVersion.getModVersion());
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getVersionString() {
        return _mcVer.getVersionString() + "-" + _modVer.getVersionString();
    }

    @Override
    public boolean containsVersion(ArtifactVersion artifactVersion) {
        return compareTo(artifactVersion) == 0;
    }

    @Override
    public String getRangeString() {
        return null;
    }

    @Override
    public String toString() {
        return _modVer.toString() + " for " + _mcVer.toString();
    }

    public ReleaseVersion getMinecraftVersion() {
        return _mcVer;
    }

    public ReleaseVersion getModVersion() {
        return _modVer;
    }

    public String getDescription() {
        return description;
    }
}