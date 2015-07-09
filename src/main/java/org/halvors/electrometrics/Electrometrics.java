package org.halvors.electrometrics;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.ItemRetriever;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.Tab;
import org.halvors.electrometrics.common.base.Tier.ElectricityMeterTier;
import org.halvors.electrometrics.common.block.BlockElectricityMeter;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.event.PlayerEventHandler;
import org.halvors.electrometrics.common.item.ItemBlockElectricityMeter;
import org.halvors.electrometrics.common.tile.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.UnitDisplay.Unit;

import java.io.File;

/**
 * This is the Electrometrics class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "after:CoFHCore;" +
																							  "after:Mekanism")
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
	public static final BlockMachine blockElectricityMeter = new BlockElectricityMeter();

	// Configuration.
	private static Configuration configuration;

	// Configuration variables.
	public static Unit energyType = Unit.JOULES;
	public static double toJoules;
	public static double toMinecraftJoules;
	public static double toElectricalUnits;

	// Mod integration.
	private boolean isMekanismIntegrationEnabled;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File config = event.getSuggestedConfigurationFile();

		// Set the mod's configuration
		configuration = new Configuration(config);
		configuration.load();

		isMekanismIntegrationEnabled = configuration.get(Configuration.CATEGORY_GENERAL, "MekanismIntegration", Loader.isModLoaded("Mekanism")).getBoolean();

		String energyTypeString = configuration.get(Configuration.CATEGORY_GENERAL, "EnergyType", "J", "The default energy system to display.", new String[] { "RF", "J", "MJ", "EU" }).getString();

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

		// Mod integration.
		logger.log(Level.INFO, "Mekanism integration is " + (isMekanismIntegrationEnabled ? "enabled" : "disabled") + ".");
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
		if (isMekanismIntegrationEnabled) {
			// Add recipe for all tiers.
			for (ElectricityMeterTier tier : ElectricityMeterTier.values()) {
				ItemStack itemStackElectricityMeter = new ItemStack(blockElectricityMeter);
				ItemBlockElectricityMeter itemBlockElectricityMeter = (ItemBlockElectricityMeter) itemStackElectricityMeter.getItem();
				itemBlockElectricityMeter.setTier(itemStackElectricityMeter, tier);

				ItemStack universalCable = new ItemStack(ItemRetriever.getItem("PartTransmitter").getItem(), 8, tier.ordinal());

				GameRegistry.addRecipe(itemStackElectricityMeter,
						"III",
						"UCU",
						"III", 'I', Items.iron_ingot, 'U', universalCable, 'C', Items.clock);
			}
		} else {
			GameRegistry.addRecipe(new ItemStack(blockElectricityMeter),
				"III",
				"RCR",
				"III", 'I', Items.iron_ingot, 'R', Items.redstone, 'C', Items.clock);
		}
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