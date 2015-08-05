package org.halvors.electrometrics.common.updater;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ReleaseVersion implements ArtifactVersion {
    private final String label;
    private final int major;
    private final int minor;
    private final int patch;
    private final int _rc;
    private final int _beta;

    public ReleaseVersion(String label, int major, int minor, int patch, int rc, int beta) {
        this.label = label;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        _rc = rc;
        _beta = beta;
    }

    public ReleaseVersion(String label, String s) {
        int major = 0;
        int minor = 0;
        int patch = 0;
        int rc = 0;
        int beta = 0;
        String[] parts = s.split("RC");

        if (parts.length > 1) {
            rc = Integer.parseInt(parts[1]);
            s = parts[0];
        }

        parts = s.split("B");

        if (parts.length > 1) {
            beta = Integer.parseInt(parts[1]);
            s = parts[0];
        }

        parts = s.split("\\.");

        switch (parts.length) {
            case 3:
                patch = Integer.parseInt(parts[2]);
            case 2:
                minor = Integer.parseInt(parts[1]);
            case 1:
                major = Integer.parseInt(parts[0]);
        }

        this.label = label;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        _rc = rc;
        _beta = beta;
    }

    public static ReleaseVersion parse(String label, String s) {
        return new ReleaseVersion(label, s);
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

        if (patch != releaseVersion.getPatch()) {
            return patch < releaseVersion.getPatch() ? -1 : 1;
        }

        if (this.isStable() && !releaseVersion.isStable()) {
            return 1;
        }

        if (this.isRC() && releaseVersion.isBeta()) {
            return 1;
        }

        if (!this.isStable() && releaseVersion.isStable()) {
            return -1;
        }

        if (this.isBeta() && releaseVersion.isRC()) {
            return -1;
        }

        if (this.rc() != releaseVersion.rc()) {
            return this.rc() < releaseVersion.rc() ? -1 : 1;
        }

        if (this.beta() != releaseVersion.beta()) {
            return this.beta() < releaseVersion.beta() ? -1 : 1;
        }

        return 0;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getVersionString() {
        String v = major + "." + minor + "." + patch;

        if (_rc != 0) {
            v += "RC" + _rc;
        }

        if (_beta != 0) {
            v += "B" + _beta;
        }

        return v;
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
        return label + " " + getVersionString();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public int rc() {
        return _rc;
    }

    public int beta() {
        return _beta;
    }

    public boolean isStable() {
        return _rc == 0 & _beta == 0;
    }

    public boolean isRC() {
        return _rc > 0;
    }

    public boolean isBeta() {
        return _beta > 0;
    }
}
