package org.halvors.ElectricityMeter;

import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.component.Category;
import nova.core.game.ClientManager;
import nova.core.gui.factory.GuiFactory;
import nova.core.gui.factory.GuiManager;
import nova.core.item.ItemFactory;
import nova.core.item.ItemManager;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.core.nativewrapper.NativeManager;
import nova.core.network.NetworkManager;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.ItemIngredient;
import nova.core.recipes.crafting.ShapedCraftingRecipe;
import nova.core.render.RenderManager;
import nova.core.render.texture.BlockTexture;
import org.halvors.ElectricityMeter.block.BlockElectricityMeter;
import org.halvors.ElectricityMeter.gui.GuiBasic;
import org.halvors.ElectricityMeter.gui.GuiElectricityMeter;

import java.util.ArrayList;
import java.util.List;

/**
 * ConcentratedSolars is a NOVA mod that adds solar power sources to the game.
 *
 * @author halvors
 */
@NovaMod(id = Reference.ID, name = Reference.NAME, version = Reference.VERSION, novaVersion = Reference.NOVA_VERSION)
public class ElectricityMeter implements Loadable {
	private static ElectricityMeter instance;

	// Blocks
	public static BlockFactory blockElectricityMeter;

	// Items
	public static ItemFactory itemElectricityMeter;

	// Textures
	public static BlockTexture blockElectricityMeterTexture;
	public static BlockTexture blockElectricityMeterTextureSide;

	// GUIs
	public static GuiFactory guiBasic;
	public static GuiFactory guiElectricityMeter;

	// Category
	public final Category category = new Category("categoryElectricityMeter");

	// Managers
	public final BlockManager blockManager;
	public final ItemManager itemManager;
	public final RenderManager renderManager;
	public final NativeManager nativeManager;
	public final NetworkManager networkManager;
	public final RecipeManager recipeManager;
	public final GuiManager guiManager;

	public ElectricityMeter(BlockManager blockManager,
							ItemManager itemManager,
							RenderManager renderManager,
							NativeManager nativeManager,
							NetworkManager networkManager,
							RecipeManager recipeManager,
							GuiManager guiManager) {
		instance = this;

		this.blockManager = blockManager;
		this.itemManager = itemManager;
		this.renderManager = renderManager;
		this.nativeManager = nativeManager;
		this.networkManager = networkManager;
		this.recipeManager = recipeManager;
		this.guiManager = guiManager;
	}

	@Override
	public void preInit() {
		addBlocks();
		addItems();
		addTextures();
		addRecipes();
		addGUIs();
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	/**
	 * Returns the instance of this mod.
	 */
	public static ElectricityMeter getInstance() {
		return instance;
	}

	public void addBlocks() {
		// Create blocks.
		blockElectricityMeter = blockManager.register(BlockElectricityMeter.class);
	}

	public void addItems() {
		// Create items.
		itemElectricityMeter = itemManager.getItemFromBlock(blockElectricityMeter);
	}

	public void addTextures() {
		// Create textures.
		blockElectricityMeterTexture = renderManager.registerTexture(new BlockTexture(Reference.ID, "blockElectricityMeter"));
		blockElectricityMeterTextureSide = renderManager.registerTexture(new BlockTexture(Reference.ID, "blockElectricityMeterSide"));
	}

	public void addRecipes() {
		// Create recipes.
		ItemIngredient glassPaneIngredient = ItemIngredient.forDictionary("paneGlass");
		ItemIngredient ironIngotIngredient = ItemIngredient.forDictionary("ingotIron");
		ItemIngredient goldIngotIngredient = ItemIngredient.forDictionary("ingotGold");
		ItemIngredient dustRedstoneIngredient = ItemIngredient.forDictionary("dustRedstone");

		// Register recipes.
		recipeManager.addRecipe(new ShapedCraftingRecipe(itemElectricityMeter.makeItem(), "AAA-BCB-BDB",
				glassPaneIngredient,
				ironIngotIngredient,
				goldIngotIngredient,
				dustRedstoneIngredient));
	}

	public void addGUIs() {
		// Create GUIs.
		guiElectricityMeter = guiManager.register(() -> new GuiElectricityMeter());
	}
}