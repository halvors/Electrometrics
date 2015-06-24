package org.halvors.electrometrics;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.CreativeTab;
import org.halvors.electrometrics.common.PacketHandler;
import org.halvors.electrometrics.common.block.BlockElectricityMeter;
import org.halvors.electrometrics.common.tileentity.TileEntityElectricityMeter;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "after:CoFHCore")
public class Electrometrics {
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors.electrometrics.client.ClientProxy", serverSide = "org.halvors.electrometrics.common.CommonProxy")
	private static CommonProxy proxy;

	// The instance of your mod that Forge uses.
	@Instance(value = Reference.ID)
	private static Electrometrics instance;

	// Packet handler.
	private static PacketHandler packetHandler = new PacketHandler();

	// Creative tab
	public static CreativeTab tabElectrometrics = new CreativeTab();

	// Blocks
	public static Block blockElectricityMeter;

	// Items

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Nothing to see here.
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Packet registrations
		packetHandler.initialize();

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
		GameRegistry.registerBlock(blockElectricityMeter, "blockElectricityMeter");
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

	public static PacketHandler getPacketHandler() {
		return packetHandler;
	}
}