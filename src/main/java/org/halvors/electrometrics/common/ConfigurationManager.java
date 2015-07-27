package org.halvors.electrometrics.common;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.util.energy.EnergyUnit;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    public static final String CATEGORY_MACHINE = "machine";
    public static final String CATEGORY_INTEGRATION = "integration";
    public static final String CATEGORY_CLIENT = "client";

    public static class General {
        public static boolean destroyDisabledBlocks;

        public static double toJoules;
        public static double toMinecraftJoules;
        public static double toElectricalUnits;
    }

    public static class Machine {
        private static final Map<MachineType, Boolean> machines = new HashMap<>();

        public static boolean isEnabled(MachineType machineType) {
            return machines.get(machineType) != null && machines.get(machineType);
        }

        public static void setEntry(MachineType machineType, boolean isEnabled) {
            machines.put(machineType, isEnabled);
        }
    }

    public static class Integration {
        public static boolean isMekanismEnabled;
}

    public static class Client {
        public static EnergyUnit energyUnit;
    }

    public static void loadConfiguration(Configuration configuration) {
        configuration.load();

        // General.
        General.destroyDisabledBlocks = configuration.get(Configuration.CATEGORY_GENERAL, "DestroyDisabledBlocks", true).getBoolean();

        General.toJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToJoules", 2.5).getDouble();
        General.toMinecraftJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToMinecraftJoules", 0.1).getDouble();
        General.toElectricalUnits = configuration.get(Configuration.CATEGORY_GENERAL, "RFToElectricalUnits", 0.25).getDouble();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, configuration.get(CATEGORY_MACHINE, machineType.getUnlocalizedName() + "Enabled", true).getBoolean());
        }

        // Integration.
        Integration.isMekanismEnabled = configuration.get(CATEGORY_INTEGRATION, "Mekanism", Loader.isModLoaded("Mekanism")).getBoolean();

        // Client.
        Client.energyUnit = EnergyUnit.getUnitFromSymbol(configuration.get(CATEGORY_CLIENT, "EnergyUnitType", EnergyUnit.JOULES.getName(), "The default energy system to display.", EnergyUnit.getNames().toArray(new String[EnergyUnit.getNames().size()])).getString());

        configuration.save();
    }
}
