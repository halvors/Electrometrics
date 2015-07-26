package org.halvors.electrometrics.common;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.util.energy.EnergyUnit;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    public static final String CATEGORY_MACHINE = "machine";
    public static final String CATEGORY_CLIENT = "client";

    public static class General {
        public static boolean destroyDisabledBlocks;

        // Energy.
        public static EnergyUnit energyUnitType;
        public static double toJoules;
        public static double toMinecraftJoules;
        public static double toElectricalUnits;

        // Mod integration.
        public static boolean isMekanismIntegrationEnabled;
    }

    public static class Machine {
        private static final Map<MachineType, Boolean> machines = new HashMap<>();

        public static boolean isEnabled(MachineType machineType) {
            return machines.get(machineType.getUnlocalizedName()) != null && machines.get(machineType.getUnlocalizedName());
        }

        public static void setEntry(MachineType machineType, boolean isEnabled) {
            machines.put(machineType, isEnabled);
        }
    }

    public static class Client {

    }

    public static void loadConfiguration(Configuration configuration) {
        configuration.load();

        // General.
        General.destroyDisabledBlocks = configuration.get("general", "DestroyDisabledBlocks", true).getBoolean();

        General.energyUnitType = EnergyUnit.getUnitFromSymbol(configuration.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "EnergyUnitType", "J", "The default energy system to display.", new String[]{"RF", "J", "MJ", "EU"}).getString());
        General.toJoules = configuration.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "RFToJoules", 2.5).getDouble();
        General.toMinecraftJoules = configuration.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "RFToMinecraftJoules", 0.1).getDouble();
        General.toElectricalUnits = configuration.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "RFToElectricalUnits", 0.25).getDouble();

        General.isMekanismIntegrationEnabled = configuration.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "MekanismIntegration", Loader.isModLoaded("Mekanism")).getBoolean();

        // Machines.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, configuration.get(CATEGORY_MACHINE, machineType.getUnlocalizedName() + "Enabled", true).getBoolean());
        }

        configuration.save();
    }
}
