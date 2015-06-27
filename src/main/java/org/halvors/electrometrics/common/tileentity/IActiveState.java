package org.halvors.electrometrics.common.tileentity;

/**
 * Implement this if your TileEntity has some form of active state.
 *
 * @author halvors
 */
public interface IActiveState {
    /**
     * Gets the active state as a boolean.
     */
    boolean isActive();

    /**
     * Sets the active state to a new value.
     */
    void setActive(boolean isActive);
}
