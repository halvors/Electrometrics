package org.halvors.ElectricityMeter;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.MekanismAPI;
import mekanism.api.MekanismConfig;
import mekanism.api.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.ElectricityMeter.common.CommonProxy;
import org.halvors.ElectricityMeter.common.block.BlockElectricityMeter;
import org.halvors.ElectricityMeter.common.item.ItemElectricityMeter;
import org.halvors.ElectricityMeter.common.tileentity.TileEntityElectricityMeter;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION)
public class ElectricityMeter {
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors.ElectricityMeter.client.ClientProxy", serverSide = "org.halvors.ElectricityMeter.common.CommonProxy")
	public static CommonProxy proxy;

	// The instance of your mod that Forge uses.
	@Instance(value = Reference.ID)
	public static ElectricityMeter instance;

	// Blocks
	public static Block blockElectricityMeter;

	// Items

	// Creative tab
	public static CreativeTabElectricityMeter tabElectricityMeter = new CreativeTabElectricityMeter();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();	
		
		// Register GUI handler.
		//NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		// Call functions for adding blocks etc.
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
		GameRegistry.registerBlock(blockElectricityMeter, ItemElectricityMeter.class, "blockElectricityMeter");
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
}