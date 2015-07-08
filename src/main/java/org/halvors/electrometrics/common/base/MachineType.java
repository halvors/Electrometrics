package org.halvors.electrometrics.common.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.halvors.electrometrics.Electrometrics;
import org.halvors.electrometrics.common.base.Tier.ElectricityMeterTier;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.tile.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.tile.TileEntityMachine;
import org.halvors.electrometrics.common.util.Utils;

public enum MachineType {
    BASIC_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "BasicElectricityMeter", 0, TileEntityElectricityMeter.class),
    ADVANCED_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "AdvancedElectricityMeter", 1, TileEntityElectricityMeter.class),
    ELITE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "EliteElectricityMeter", 2, TileEntityElectricityMeter.class),
    ULTIMATE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "UltimateElectricityMeter", 3, TileEntityElectricityMeter.class),
    CREATIVE_ELECTRICITY_METER(Electrometrics.blockElectricityMeter, "CreativeElectricityMeter", 4, TileEntityElectricityMeter.class);

    private final String name;
    private final BlockMachine block;
    private final int metadata;
    private final Class<? extends TileEntityMachine> tileEntityClass;

    MachineType(BlockMachine block, String name, int metadata, Class<? extends TileEntityMachine> tileEntityClass) {
        this.name = name;
        this.block = block;
        this.metadata = metadata;
        this.tileEntityClass = tileEntityClass;
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

    public TileEntity getTileEntity() {
        try {
            switch (this) {
                case BASIC_ELECTRICITY_METER:
                case ADVANCED_ELECTRICITY_METER:
                case ELITE_ELECTRICITY_METER:
                case ULTIMATE_ELECTRICITY_METER:
                case CREATIVE_ELECTRICITY_METER:
                    ElectricityMeterTier electricityMeterTier = ElectricityMeterTier.values()[metadata];
                    String name = Utils.translate("tile." + electricityMeterTier.getBaseTier().getName() + block.getName() + ".name");

                    return tileEntityClass.getConstructor(new Class[] { String.class, ElectricityMeterTier.class }).newInstance(name, electricityMeterTier);

                default:
                    return tileEntityClass.newInstance();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Electrometrics.getLogger().error("Unable to indirectly create tile entity.");
        }

        return null;
    }

    public Class<? extends TileEntityMachine> getTileEntityClass() {
        return tileEntityClass;
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