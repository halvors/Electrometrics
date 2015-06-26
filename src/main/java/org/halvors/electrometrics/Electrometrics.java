package org.halvors.electrometrics;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.CreativeTab;
import org.halvors.electrometrics.common.event.PlayerEventHandler;
import org.halvors.electrometrics.common.UnitDisplay;
import org.halvors.electrometrics.common.UnitDisplay.Unit;
import org.halvors.electrometrics.common.block.BlockElectricityMeter;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.network.PacketHandler;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

import java.io.File;

/**
 * This is the Electrometrics class, whick is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "after:CoFHCore")
public class Electrometrics {
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors.electrometrics.client.ClientProxy", serverSide = "org.halvors.electrometrics.common.CommonProxy")
	private static CommonProxy proxy;

	// The instance of your mod that Forge uses.
	@Instance(value = Reference.ID)
	private static Electrometrics instance;

	// Logger instance.
	private static Logger logger = LogManager.getLogger(Reference.ID);

	// Packet handler.
	private static PacketHandler packetHandler = new PacketHandler();

	// Configuration.
	private static Configuration configuration;

	// Creative tab.
	public static CreativeTab tabElectrometrics = new CreativeTab();

	// Blocks.
	public static Block blockElectricityMeter;

	// Items.

	// Variables.
	public static Unit energyType = Unit.JOULES;
	public static double toJoules;
	public static double toMinecraftJoules;
	public static double toElectricalUnits;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File config = event.getSuggestedConfigurationFile();

		// Set the mod's configuration
		configuration = new Configuration(config);
		configuration.load();

		String energyTypeString = configuration.get(Configuration.CATEGORY_GENERAL, "energyType", "J", null, new String[] { "RF", "J", "MJ", "EU" }).getString();

		if (energyTypeString != null) {
			if (energyTypeString.trim().equalsIgnoreCase("RF")) {
				energyType = Unit.REDSTONE_FLUX;
			} else if (energyTypeString.trim().equalsIgnoreCase("J")) {
				energyType = Unit.JOULES;
			} else if (energyTypeString.trim().equalsIgnoreCase("MJ")) {
				energyType = Unit.MINECRAFT_JOULES;
			} else if (energyTypeString.trim().equalsIgnoreCase("EU")) {
				energyType = Unit.ELECTRICAL_UNITS;
			}
		}

		toJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToJoules", 2.5).getDouble(); // Ok?
		toMinecraftJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToMinecraftJoules", 0.1).getDouble(); // Ok?

		toElectricalUnits = configuration.get(Configuration.CATEGORY_GENERAL, "RFToElectricalUnits", 0.4).getDouble(); // Ok?, not checked...

		configuration.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();

		// Packet registrations
		packetHandler.initialize();

		// Register the our EventHandler.
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Call functions for adding blocks, items, etc.
		addBlocks();
		addItems();
		addTileEntities();
		addRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Nothing to see here.
	}

	public void addBlocks() {
		// Create blocks.
		blockElectricityMeter = new BlockElectricityMeter();

		// Register blocks.
		GameRegistry.registerBlock(blockElectricityMeter, ItemBlockElectricityMeter.class, "blockElectricityMeter");
	}

	public void addItems() {
		// Create items.

		// Register items.
	}

	public void addTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileEntityElectricityMeter");
	}

	public void addRecipes() {
		// Register recipes.
		GameRegistry.addRecipe(new ItemStack(blockElectricityMeter),
				"III",
				"RCR",
				"III", 'I', Items.iron_ingot, 'C', Items.clock, 'R', Items.redstone);
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static Electrometrics getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static PacketHandler getPacketHandler() {
		return packetHandler;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	/*
     * Converts the energy to the default energy system.
     */
	public static String getEnergyDisplay(double energy) {
		switch (energyType) {
			case REDSTONE_FLUX:
				return UnitDisplay.getDisplayShort(energy, Unit.REDSTONE_FLUX);

			case JOULES:
				return UnitDisplay.getDisplayShort(energy * toJoules, Unit.JOULES);

			case MINECRAFT_JOULES:
				return UnitDisplay.getDisplayShort(energy * toMinecraftJoules / 10, Unit.MINECRAFT_JOULES);

			case ELECTRICAL_UNITS:
				return UnitDisplay.getDisplayShort(energy * toElectricalUnits, Unit.MINECRAFT_JOULES);
		}

		return null;
	}
}