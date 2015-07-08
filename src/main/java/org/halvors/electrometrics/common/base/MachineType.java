package org.halvors.electrometrics.common.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.tile.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.tile.TileEntityMachine;

public enum MachineType {
    BASIC_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "BasicElectricityMeter", 0, TileEntityElectricityMeter.class),
    ADVANCED_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "AdvancedElectricityMeter", 1, TileEntityElectricityMeter.class),
    ELITE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "EliteElectricityMeter", 2, TileEntityElectricityMeter.class),
    ULTIMATE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "UltimateElectricityMeter", 3, TileEntityElectricityMeter.class),
    CREATIVE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "CreativeElectricityMeter", 4, TileEntityElectricityMeter.class);

    private String name;
    private Block block;
    private int metadata;
    private Class<? extends TileEntityMachine> tileEntityClass;

    MachineType(Block block, String name, int metadata, Class<? extends TileEntityMachine> tileEntityClass) {
        this.name = name;
        this.block = block;
        this.metadata = metadata;
        this.tileEntityClass = tileEntityClass;
    }

    public String getName() {
        return name;
    }

    public Block getBlock() {
        return block;
    }

    public int getMetadata() {
        return metadata;
    }

    public TileEntity getTileEntity() {
        try {
            return tileEntityClass.newInstance();
        } catch(Exception e) {
            e.printStackTrace();
            Electrometrics.getLogger().error("Unable to indirectly create tile entity.");
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