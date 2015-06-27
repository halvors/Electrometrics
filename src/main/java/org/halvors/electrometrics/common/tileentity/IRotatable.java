package org.halvors.electrometrics.common.tileentity;

/**
 * This makes a TileEntity rotable, it's meant to be extended.
 *
 * @author halvors
 */
public interface IRotatable {
    boolean canSetFacing(int facing);

    short getFacing();

    void setFacing(short facing);
}