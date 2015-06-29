package org.halvors.electrometrics.common.util;

import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.UnitDisplay;
import org.halvors.electrometrics.common.tileentity.IRedstoneControl;

public class Utils {
    /*
	 * Converts the energy to the default energy system.
	 */
    public static String getEnergyDisplay(double energy) {
        switch (Electrometrics.energyType) {
            case REDSTONE_FLUX:
                return UnitDisplay.getDisplayShort(energy, UnitDisplay.Unit.REDSTONE_FLUX);

            case JOULES:
                return UnitDisplay.getDisplayShort(energy * Electrometrics.toJoules, UnitDisplay.Unit.JOULES);

            case MINECRAFT_JOULES:
                return UnitDisplay.getDisplayShort(energy * Electrometrics.toMinecraftJoules / 10, UnitDisplay.Unit.MINECRAFT_JOULES);

            case ELECTRICAL_UNITS:
                return UnitDisplay.getDisplayShort(energy * Electrometrics.toElectricalUnits, UnitDisplay.Unit.MINECRAFT_JOULES);
        }

        return null;
    }

    /**
     * Whether or not a certain TileEntity can function with redstone logic. Illogical to use unless the defined TileEntity implements
     * IRedstoneControl.
     * @param tileEntity - TileEntity to check
     * @return if the TileEntity can function with redstone logic
     */
    public static boolean canFunction(TileEntity tileEntity) {
        if (tileEntity instanceof IRedstoneControl) {
            IRedstoneControl redstoneControl = (IRedstoneControl) tileEntity;

            switch (redstoneControl.getControlType()) {
                case DISABLED:
                    return true;

                case HIGH:
                    return redstoneControl.isPowered();

                case LOW:
                    return !redstoneControl.isPowered();

                case PULSE:
                    return redstoneControl.isPowered() && !redstoneControl.wasPowered();
            }
        }

        return false;
    }
}
