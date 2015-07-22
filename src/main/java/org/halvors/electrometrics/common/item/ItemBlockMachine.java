package org.halvors.electrometrics.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.halvors.electrometrics.client.key.Key;
import org.halvors.electrometrics.client.key.KeyHandler;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.Tier;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.base.tile.RedstoneControlType;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.tile.TileEntityComponentContainer;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.util.LanguageUtils;
import org.halvors.electrometrics.common.util.energy.EnergyUtils;
import org.halvors.electrometrics.common.util.render.Color;

import java.util.List;

public class ItemBlockMachine extends ItemBlock {
    public ItemBlockMachine(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        MachineType machineType = MachineType.getType(itemStack);

        return machineType.getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        MachineType machineType = MachineType.getType(itemStack);

        return machineType.getLocalizedName();
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        MachineType machineType = MachineType.getType(itemStack);

        if (!KeyHandler.getIsKeyPressed(Key.SNEAK.getKeyBinding())) {
            list.add(LanguageUtils.translate("tooltip.hold") + " " + Color.AQUA + GameSettings.getKeyDisplayString(Key.SNEAK.getKeyBinding().getKeyCode()) + Color.GREY + " " + LanguageUtils.translate("tooltip.forDetails") + ".");
        } else {
            switch (machineType) {
                case BASIC_ELECTRICITY_METER:
                case ADVANCED_ELECTRICITY_METER:
                case ELITE_ELECTRICITY_METER:
                case ULTIMATE_ELECTRICITY_METER:
                case CREATIVE_ELECTRICITY_METER:
                    list.add(Color.BRIGHT_GREEN + LanguageUtils.translate("tooltip.measuredEnergy") + ": " + Color.GREY + EnergyUtils.getEnergyDisplay(getElectricityCount(itemStack)));
                    list.add(Color.AQUA + LanguageUtils.translate("tooltip.storedEnergy") + ": " + Color.GREY + EnergyUtils.getEnergyDisplay(getElectricityStored(itemStack)));
                    break;

                default:
                    list.add(Color.RED + LanguageUtils.translate("tooltip.noInformation"));
                    break;
            }
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        boolean placed = super.placeBlockAt(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

        if (placed) {
            TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

            if (tileEntity instanceof TileEntityComponentContainer) {
                TileEntityComponentContainer tileEntityComponentContainer = (TileEntityComponentContainer) tileEntity;

                if (tileEntityComponentContainer.hasComponentImplementing(ITileRedstoneControl.class)) {
                    ITileRedstoneControl tileRedstoneControl = (ITileRedstoneControl) tileEntityComponentContainer.getComponentImplementing(ITileRedstoneControl.class);

                    tileRedstoneControl.setControlType(getRedstoneControlType(itemStack));
                }
            }

            if (tileEntity instanceof TileEntityElectricityMeter) {
                TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;
                tileEntityElectricityMeter.setTier(getElectricityMeterTier(itemStack));
                tileEntityElectricityMeter.setElectricityCount(getElectricityCount(itemStack));
                tileEntityElectricityMeter.getStorage().setEnergyStored(getElectricityStored(itemStack));
            }
        }

        return placed;
    }

    private RedstoneControlType getRedstoneControlType(ItemStack itemStack) {
        if (itemStack.stackTagCompound != null) {
            return RedstoneControlType.values()[itemStack.stackTagCompound.getInteger("redstoneControlType")];
        }

        return RedstoneControlType.DISABLED;
    }

    public void setRedstoneControlType(ItemStack itemStack, RedstoneControlType redstoneControlType) {
        if (itemStack.stackTagCompound == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.stackTagCompound.setInteger("redstoneControlType", redstoneControlType.ordinal());
    }

    private Tier.ElectricityMeter getElectricityMeterTier(ItemStack itemStack) {
        if (itemStack.stackTagCompound != null) {
            return Tier.ElectricityMeter.values()[itemStack.stackTagCompound.getInteger("tier")];
        }

        return Tier.ElectricityMeter.BASIC;
    }

    public void setElectricityMeterTier(ItemStack itemStack, Tier.ElectricityMeter tier) {
        if (itemStack.stackTagCompound == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.stackTagCompound.setInteger("tier", tier.getBase().ordinal());
    }

    private double getElectricityCount(ItemStack itemStack) {
        MachineType machineType = MachineType.getType(itemStack);

        switch (machineType) {
            case BASIC_ELECTRICITY_METER:
            case ADVANCED_ELECTRICITY_METER:
            case ELITE_ELECTRICITY_METER:
            case ULTIMATE_ELECTRICITY_METER:
            case CREATIVE_ELECTRICITY_METER:
                if (itemStack.stackTagCompound != null) {
                    return itemStack.stackTagCompound.getDouble("electricityCount");
                }

            default:
                return 0;
        }
    }

    public void setElectricityCount(ItemStack itemStack, double electricityCount) {
        MachineType machineType = MachineType.getType(itemStack);

        switch (machineType) {
            case BASIC_ELECTRICITY_METER:
            case ADVANCED_ELECTRICITY_METER:
            case ELITE_ELECTRICITY_METER:
            case ULTIMATE_ELECTRICITY_METER:
            case CREATIVE_ELECTRICITY_METER:
                if (itemStack.stackTagCompound == null) {
                    itemStack.setTagCompound(new NBTTagCompound());
                }

                itemStack.stackTagCompound.setDouble("electricityCount", electricityCount);
                break;
        }
    }

    private int getElectricityStored(ItemStack itemStack) {
        MachineType machineType = MachineType.getType(itemStack);

        switch (machineType) {
            case BASIC_ELECTRICITY_METER:
            case ADVANCED_ELECTRICITY_METER:
            case ELITE_ELECTRICITY_METER:
            case ULTIMATE_ELECTRICITY_METER:
            case CREATIVE_ELECTRICITY_METER:
                if (itemStack.stackTagCompound != null) {
                    return itemStack.stackTagCompound.getInteger("electricityStored");
                }

            default:
                return 0;
        }
    }

    public void setElectricityStored(ItemStack itemStack, int electricityStored) {
        MachineType machineType = MachineType.getType(itemStack);

        switch (machineType) {
            case BASIC_ELECTRICITY_METER:
            case ADVANCED_ELECTRICITY_METER:
            case ELITE_ELECTRICITY_METER:
            case ULTIMATE_ELECTRICITY_METER:
            case CREATIVE_ELECTRICITY_METER:
                if (itemStack.stackTagCompound == null) {
                    itemStack.setTagCompound(new NBTTagCompound());
                }

                itemStack.stackTagCompound.setInteger("electricityStored", electricityStored);
                break;
        }
    }
}
