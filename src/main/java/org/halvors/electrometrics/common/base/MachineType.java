package org.halvors.electrometrics.common.base;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.client.gui.machine.GuiElectricityMeter;
import org.halvors.electrometrics.common.ConfigurationManager.Machine;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.tile.machine.TileEntityMachine;
import org.halvors.electrometrics.common.util.LanguageUtils;

public enum MachineType {
	BASIC_ELECTRICITY_METER(0, "ElectricityMeter", TileEntityElectricityMeter.class, GuiElectricityMeter.class),
	ADVANCED_ELECTRICITY_METER(1, "ElectricityMeter", TileEntityElectricityMeter.class, GuiElectricityMeter.class),
	ELITE_ELECTRICITY_METER(2, "ElectricityMeter", TileEntityElectricityMeter.class, GuiElectricityMeter.class),
	ULTIMATE_ELECTRICITY_METER(3, "ElectricityMeter", TileEntityElectricityMeter.class, GuiElectricityMeter.class);

	private final int metadata;
	private final String name;
	private final Class<? extends TileEntityMachine> tileEntityClass;
	private final Class<? extends GuiScreen> guiClass;

	MachineType(int metadata, String name, Class<? extends TileEntityMachine> tileEntityClass, Class<? extends GuiScreen> guiClass) {
		this.metadata = metadata;
		this.name = name;
		this.tileEntityClass = tileEntityClass;
		this.guiClass = guiClass;
	}

	public String getUnlocalizedName() {
		switch (this) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				Tier.Base baseTier = Tier.Electric.getFromMachineType(this).getBase();

				return baseTier.getUnlocalizedName() + name;

			default:
				return name;
		}
	}

	public String getLocalizedName() {
		String localizedName = LanguageUtils.localize("tile." + name + ".name");

		switch (this) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				Tier.Base baseTier = Tier.Electric.getFromMachineType(this).getBase();

				return baseTier.getLocalizedName() + " " + localizedName;

			default:
				return localizedName;
		}
	}

	public int getMetadata() {
		return metadata;
	}

	public TileEntityMachine getTileEntity() {
		try {
			switch (this) {
				case BASIC_ELECTRICITY_METER:
				case ADVANCED_ELECTRICITY_METER:
				case ELITE_ELECTRICITY_METER:
				case ULTIMATE_ELECTRICITY_METER:
					return tileEntityClass.getConstructor(MachineType.class, Tier.Electric.class).newInstance(this, Tier.Electric.getFromMachineType(this));

				default:
					return tileEntityClass.newInstance();
			}
		} catch(Exception e) {
			e.printStackTrace();
			Electrometrics.getLogger().error("Unable to indirectly create TileEntity.");
		}

		return null;
	}

	public GuiScreen getGui(TileEntityMachine tileEntity) {
		try {
			return guiClass.getConstructor(tileEntityClass).newInstance(tileEntity);
		} catch(Exception e) {
			e.printStackTrace();
			Electrometrics.getLogger().error("Unable to indirectly create GuiScreen.");
		}

		return null;
	}

	public ItemStack getItemStack() {
		return new ItemStack(Electrometrics.blockMachine, 1, metadata);
	}

	public Item getItem() {
		return getItemStack().getItem();
	}

	public boolean isEnabled() {
		return Machine.isEnabled(this);
	}

	public static MachineType getType(Block block, int metadata) {
		if (block instanceof BlockMachine) {
			for (MachineType machineType : values()) {
				if (metadata == machineType.getMetadata()) {
					return machineType;
				}
			}
		}

		return null;
	}

	public static MachineType getType(ItemStack itemStack) {
		return getType(Block.getBlockFromItem(itemStack.getItem()), itemStack.getMetadata());
	}
}