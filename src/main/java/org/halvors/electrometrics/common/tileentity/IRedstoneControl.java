package org.halvors.electrometrics.common.tileentity;

public interface IRedstoneControl {
    public static enum RedstoneControlType {
        DISABLED("Disabled"),
        HIGH("High"),
        LOW("Low"),
        PULSE("Pulse");

        private String display;

        public String getDisplay() {
            return display;
        }

        private RedstoneControlType(String s) {
            display = s;
        }
    }

    /**
     * Gets the RedstoneControl type from this block.
     * @return this block's RedstoneControl type
     */
    public RedstoneControlType getControlType();

    /**
     * Sets this block's RedstoneControl type to a new value.
     * @param type - RedstoneControl type to set
     */
    public void setControlType(RedstoneControlType type);

    /**
     * If the block is getting powered or not by redstone (indirectly).
     * @return if the block is getting powered indirectly
     */
    public boolean isPowered();

    /**
     * If the block was getting powered or not by redstone, last tick.
     * Used for PULSE mode.
     */
    public boolean wasPowered();

    /**
     * If the machine can be pulsed.
     */
    public boolean canPulse();
}
