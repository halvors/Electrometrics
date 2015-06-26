package org.halvors.electrometrics.common.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Location {
    private World world;
    private int x;
    private int y;
    private int z;

    public Location(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public TileEntity getTileEntity() {
        return world.getTileEntity(x, y, z);
    }
}
