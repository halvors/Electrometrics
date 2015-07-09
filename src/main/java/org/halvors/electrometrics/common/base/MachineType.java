package org.halvors.electrometrics.common.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.client.gui.GuiComponentScreen;
import org.halvors.electrometrics.client.gui.GuiElectricityMeter;
import org.halvors.electrometrics.client.gui.GuiMachine;
import org.halvors.electrometrics.client.gui.IGui;
import org.halvors.electrometrics.common.base.Tier.ElectricityMeterTier;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.tile.TileEntityMachine;
import org.halvors.electrometrics.common.util.Utils;

public enum MachineType {
    BASIC_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "ElectricityMeter", 0, TileEntityElectricityMeter.class, GuiElectricityMeter.class),
    ADVANCED_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "ElectricityMeter", 1, TileEntityElectricityMeter.class, GuiElectricityMeter.class),
    ELITE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "ElectricityMeter", 2, TileEntityElectricityMeter.class, GuiElectricityMeter.class),
    ULTIMATE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "ElectricityMeter", 3, TileEntityElectricityMeter.class, GuiElectricityMeter.class),
    CREATIVE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "ElectricityMeter", 4, TileEntityElectricityMeter.class, GuiElectricityMeter.class);

    private final BlockMachine block;
    private final String name;
    private final int metadata;
    private final Class<? extends TileEntityMachine> tileEntityClass;
    private final Class<? extends IGui> guiClass;

    MachineType(BlockMachine block, String name, int metadata, Class<? extends TileEntityMachine> tileEntityClass, Class<? extends IGui> guiClass) {
        this.block = block;
        this.name = name;
        this.metadata = metadata;
        this.tileEntityClass = tileEntityClass;
        this.guiClass = guiClass;
    }

    public String getName() {
        return name;
    }

    public BlockMachine getBlock() {
        return block;
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
                case CREATIVE_ELECTRICITY_METER:
                    ElectricityMeterTier electricityMeterTier = ElectricityMeterTier.values()[metadata];
                    String name = Utils.translate("tile." + electricityMeterTier.getBaseTier().getName() + block.getName() + ".name");

                    return tileEntityClass.getConstructor(new Class[] { String.class, ElectricityMeterTier.class }).newInstance("lol", electricityMeterTier);

                default:
                    return tileEntityClass.newInstance();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Electrometrics.getLogger().error("Unable to indirectly create tile entity.");
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public IGui getGui() {
        try {
            return (IGui) guiClass.getConstructor(new Class[] { tileEntityClass }).newInstance(getTileEntity());
        } catch(Exception e) {
            e.printStackTrace();
            Electrometrics.getLogger().error("Unable to indirectly create gui.");
        }

        return null;
    }

    public static MachineType getType(Block block, int meta) {
        if (block instanceof BlockMachine) {
            for (MachineType type : values()) {
                if (meta == type.getMetadata()) {
                    return type;
                }
            }
        }

        return null;
    }

    public static MachineType getType(ItemStack itemStack) {
        return getType(Block.getBlockFromItem(itemStack.getItem()), itemStack.getItemDamage());
    }

    public ItemStack getItemStack() {
        return new ItemStack(block, 1, metadata);
    }
}