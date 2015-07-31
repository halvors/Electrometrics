package org.halvors.electrometrics.common.updater;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ModVersion implements ArtifactVersion {
    private final String label;
    private final ReleaseVersion minecraftVersion;
    private final ReleaseVersion modVersion;
    private final String description;

    public ModVersion(String label, ReleaseVersion minecraftVersion, ReleaseVersion modVersion, String description) {
        this.label = label;
        this.minecraftVersion = minecraftVersion;
        this.modVersion = modVersion;
        this.description = description;
    }

    public ModVersion(String label, String s) {
        String[] parts = s.split(" ");
        String[] versionParts = parts[0].split("-", 2);
        String description = null;

        if (parts.length > 1) {
            description = parts[1].trim();
        }

        System.out.println("parts[0] is: " +  parts[0]);

        System.out.println("versionParts[0] is: " + versionParts[0]);
        System.out.println("versionParts[1] is: " + versionParts[1]);

        this.label = label;
        this.minecraftVersion = new ReleaseVersion("Minecraft", versionParts[0]);
        this.modVersion = new ReleaseVersion(label, versionParts[1]);
        this.description = description;
    }

    public ReleaseVersion getMinecraftVersion() {
        return minecraftVersion;
    }

    public ReleaseVersion getModVersion() {
        return modVersion;
    }

    public String getDescription() {
        return description;
    }

    public static ModVersion parse(String label, String s) {
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
                return modVersion.compareTo(releaseVersion);
            } else if ("Minecraft".equals(releaseVersion.getLabel())) {
                return minecraftVersion.compareTo(releaseVersion);
            }
        }

        return 0;
    }

    public int compareTo(ModVersion modVersion) {
        if (minecraftVersion.compareTo(modVersion.getMinecraftVersion()) != 0) {
            return minecraftVersion.compareTo(modVersion.getMinecraftVersion());
        }

        return minecraftVersion.compareTo(modVersion.getModVersion());
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getVersionString() {
        return minecraftVersion.getVersionString() + "R" + modVersion.getVersionString();
    }

    @Override
    public boolean containsVersion(ArtifactVersion source) {
        return compareTo(source) == 0;
    }

    @Override
    public String toString() {
        return modVersion.toString() + " for " + minecraftVersion.toString();
    }

    @Override
    public String getRangeString() {
        return null;
    }
}
