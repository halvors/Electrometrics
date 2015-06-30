package org.halvors.electrometrics.common.tileentity;

/**
 * This makes a TileEntity rotatable, it's meant to be extended.
 *
 * @author halvors
 */
public interface IRotatable {
	boolean canSetFacing(int facing);

	int getFacing();

	void setFacing(int facing);
}