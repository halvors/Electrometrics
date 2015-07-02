package org.halvors.electrometrics;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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
import org.halvors.electrometrics.common.Tab;
import org.halvors.electrometrics.common.block.BlockElectricityMeter;
import org.halvors.electrometrics.common.event.PlayerEventHandler;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.UnitDisplay.Unit;

import java.io.File;

/**
 * This is the Electrometrics class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "after:CoFHCore")
public class Electrometrics {
	// The instance of your mod that Forge uses.
	@Instance(value = Reference.ID)
	public static Electrometrics instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors.electrometrics.client.ClientProxy", serverSide = "org.halvors.electrometrics.common.CommonProxy")
	public static CommonProxy proxy;

	// Logger instance.
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative tab.
	private static final Tab tab = new Tab();

	// Blocks.
	public static final Block blockElectricityMeter = new BlockElectricityMeter();

	// Configuration.
	private static Configuration configuration;

	// Configuration variables.
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

		String energyTypeString = configuration.get(Configuration.CATEGORY_GENERAL, "energyType", "J", "The default energy system to display.", new String[] { "RF", "J", "MJ", "EU" }).getString();

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

		toJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToJoules", 2.5).getDouble();
		toMinecraftJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToMinecraftJoules", 0.1).getDouble();
		toElectricalUnits = configuration.get(Configuration.CATEGORY_GENERAL, "RFToElectricalUnits", 0.25).getDouble();

		configuration.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register the our EventHandler.
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Call functions for adding blocks, items, etc.
		addBlocks();
		addTileEntities();
		addRecipes();
	}

	private void addBlocks() {
		// Register blocks.
		GameRegistry.registerBlock(blockElectricityMeter, ItemBlockElectricityMeter.class, "blockElectricityMeter");
	}

	private void addTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileEntityElectricityMeter");
	}

	private void addRecipes() {
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

	public static Tab getTab() {
		return tab;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}
}