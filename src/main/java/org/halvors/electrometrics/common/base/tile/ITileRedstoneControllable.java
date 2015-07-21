package org.halvors.electrometrics.common.base.tile;

public interface ITileRedstoneControllable {
	/**
	 * Gets the RedstoneControl type from this block.
	 * @return this block's RedstoneControl type
	 */
	RedstoneControllableType getControlType();

	/**
	 * Sets this block's RedstoneControl type to a new value.
	 * @param redstoneControllableType - RedstoneControl type to set
	 */
	void setControlType(RedstoneControllableType redstoneControllableType);

	/**
	 * If the block is getting powered or not by redstone (indirectly).
	 * @return if the block is getting powered indirectly
	 */
	boolean isPowered();

	/**
	 * Set the block to powered state.
	 */
	void setPowered(boolean isPowered);

	/**
	 * If the block was getting powered or not by redstone, last tick.
	 * Used for PULSE mode.
	 */
	boolean wasPowered();

	/**
	 * If the machine can be pulsed.
	 */
	boolean canPulse();
}
