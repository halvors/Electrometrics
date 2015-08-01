package org.halvors.electrometrics;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.ItemRetriever;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.ConfigurationManager;
import org.halvors.electrometrics.common.ConfigurationManager.Integration;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.Tab;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.Tier;
import org.halvors.electrometrics.common.block.Block;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.event.PlayerEventHandler;
import org.halvors.electrometrics.common.item.ItemBlockMachine;
import org.halvors.electrometrics.common.item.ItemMultimeter;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;

/**
 * This is the Electrometrics class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
     dependencies = "after:CoFHCore;" +
                    "after:Mekanism",
     guiFactory = "org.halvors." + Reference.ID + ".client.gui.configuration.GuiConfiguationFactory")
public class Electrometrics {
	// The instance of your mod that Forge uses.
	@Mod.Instance(value = Reference.ID)
	public static Electrometrics instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors.electrometrics.client.ClientProxy", serverSide = "org.halvors.electrometrics.common.CommonProxy")
	public static CommonProxy proxy;

	// Logger instance.
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative tab.
	private static final Tab tab = new Tab();

	// Items.
	public static final Item itemMultimeter = new ItemMultimeter();

	// Blocks.
	public static final Block blockMachine = new BlockMachine();

	// ConfigurationManager.
	private static Configuration configuration;


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

        // Load the configuration.
        ConfigurationManager.loadConfiguration(configuration);

		// Mod integration.
		logger.log(Level.INFO, "BuildCraft integration is " + (Integration.isBuildCraftEnabled ? "enabled" : "disabled") + ".");
		logger.log(Level.INFO, "CoFHCore integration is " + (Integration.isCoFHCoreEnabled ? "enabled" : "disabled") + ".");
		logger.log(Level.INFO, "Mekanism integration is " + (Integration.isMekanismEnabled ? "enabled" : "disabled") + ".");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// Register the our EventHandler.
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Call functions for adding blocks, items, etc.
		addItems();
		addBlocks();
		addTileEntities();
		addRecipes();
	}

	private void addItems() {
		// Register items.
		GameRegistry.registerItem(itemMultimeter, "itemMultimeter");
	}

	private void addBlocks() {
		// Register blocks.
		GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "blockMachine");
	}

	private void addTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileEntityElectricityMeter");
	}

	private void addRecipes() {
		// Register recipes.
        ItemStack cable = new ItemStack(Items.gold_ingot);
        ItemStack casing = new ItemStack(Blocks.iron_block);
		ItemStack energyStorage = new ItemStack(Blocks.diamond_block);

        if (Integration.isMekanismEnabled) {
            cable = new ItemStack(ItemRetriever.getItem("PartTransmitter").getItem(), 8); // Basic universal cable.
            casing = new ItemStack(ItemRetriever.getBlock("BasicBlock").getItem(), 1, 8); // Steel casing.
        }

        // Multimeter
        GameRegistry.addRecipe(new ShapedOreRecipe(itemMultimeter,
                "RGR",
                "IMI",
                "CBC",
                'R', "dustRedstone",
                'G', "paneGlass",
                'I', OreDictionary.doesOreNameExist("ingotCopper") ? "ingotCopper" : "ingotIron",
                'M', Items.clock,
                'C', OreDictionary.doesOreNameExist("circuitBasic") ? "circuitBasic" : Items.repeater,
                'B', OreDictionary.doesOreNameExist("battery") ? "battery" : "gemEmerald"));

        // Electricity Meter
        for (Tier.Electric electricTier : Tier.Electric.values()) {
            if (Integration.isMekanismEnabled) {
                cable = new ItemStack(ItemRetriever.getItem("PartTransmitter").getItem(), 8, electricTier.ordinal()); // Tier matching universal cable.
				energyStorage = new ItemStack(ItemRetriever.getBlock("EnergyCube").getItem(), 1, electricTier.ordinal()); // Tier matching energy cube.
			}

            MachineType machineType = electricTier.getMachineType();
			ItemStack itemStackMachine = machineType.getItemStack();
            ItemBlockMachine itemBlockMachine = (ItemBlockMachine) itemStackMachine.getItem();
            itemBlockMachine.setElectricTier(itemStackMachine, electricTier);
			Tier.Base baseTier = electricTier.getBase();

            GameRegistry.addRecipe(new ShapedOreRecipe(itemStackMachine,
                    "RMR",
                    "C#C",
                    "@B@",
                    'R', "dustRedstone",
                    'M', itemMultimeter,
                    'C', cable,
                    '#', casing,
					'@', OreDictionary.doesOreNameExist("circuit" + baseTier.getUnlocalizedName()) ? "circuit" + baseTier.getUnlocalizedName() : Items.repeater,
                    'B', energyStorage));
        }
	}

	public static Electrometrics getInstance() {
		return instance;
	}

	public static CommonProxy getProxy() {
		return proxy;
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