package org.halvors.electrometrics.common.multipart;

import codechicken.lib.data.MCDataInput;
import codechicken.multipart.MultiPartRegistry.IPartFactory2;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.Electrometrics;

public class Multipart implements IPartFactory2 {
    public void addMultiparts() {
        //MultiPartRegistry.registerParts(this, new String[] {Reference.PREFIX + "blockElectricityMeter" });

        registerMicroMaterials();
    }

    @Override
    public TMultiPart createPart(String s, NBTTagCompound nbtTagCompound) {


        return null;
    }

    @Override
    public TMultiPart createPart(String s, MCDataInput mcDataInput) {
        return null;
    }

    public void registerMicroMaterials() {
        //FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(Electrometrics.blockElectricityMeter, 1, 0));
    }
}
