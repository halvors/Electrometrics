package org.halvors.electrometrics.common.updater;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ReleaseVersion implements ArtifactVersion {
    private final String label;
    private final int major;
    private final int minor;
    private final int build;

    public ReleaseVersion(String label, int major, int minor, int build) {
        this.label = label;
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    public ReleaseVersion(String label, String s) {
        int major = 0;
        int minor = 0;
        int build = 0;
        String main = s;
        String[] parts;

        parts = main.split("\\.");

        switch (parts.length) {
            case 0:
                break;

            case 1:
                major = Integer.parseInt(parts[0]);

            case 2:
                minor = Integer.parseInt(parts[1]);

            default:
                build = Integer.parseInt(parts[2]);
        }

        this.label = label;
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    public static ReleaseVersion parse(String label, String s) {
        return new ReleaseVersion(label, s);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getBuild() {
        return build;
    }

    @Override
    public int compareTo(ArtifactVersion artifactVersion) {
        if (artifactVersion instanceof ReleaseVersion) {
            return compareTo((ReleaseVersion) artifactVersion);
        }

        if (artifactVersion instanceof ModVersion) {
            ModVersion modVersion = (ModVersion) artifactVersion;

            if (label.equals(modVersion.getLabel())) {
                return compareTo(modVersion.getModVersion());
            } else if ("Minecraft".equals(label)) {
                return compareTo(modVersion.getMinecraftVersion());
            }
        }

        return 0;
    }

    public int compareTo(ReleaseVersion releaseVersion) {
        if (major != releaseVersion.getMajor()) {
            return major < releaseVersion.getMajor() ? -1 : 1;
        }

        if (minor != releaseVersion.getMinor()) {
            return minor < releaseVersion.getMinor() ? -1 : 1;
        }

        if (build != releaseVersion.getBuild()) {
            return build < releaseVersion.getBuild() ? -1 : 1;
        }

        return 0;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getVersionString() {
        return major + "." + minor + "." + build;
    }

    @Override
    public boolean containsVersion(ArtifactVersion source) {
        return compareTo(source) == 0;
    }

    @Override
    public String getRangeString() {
        return null;
    }

    @Override
    public String toString() {
        return label + " " + getVersionString();
    }
}
