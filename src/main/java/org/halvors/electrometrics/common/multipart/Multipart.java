package org.halvors.electrometrics.common.multipart;

import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.multipart.part.PartMachine;

public class Multipart implements IPartFactory {
    public void addMultiparts() {
        MultiPartRegistry.registerParts(this, new String[] { Reference.PREFIX + "machine" });

        registerMicroMaterials();
    }

    @Override
    public TMultiPart createPart(String s, boolean b) {
        if(s.equals(Reference.PREFIX + "machine")) {
            return new PartMachine();
        }

        return null;
    }

    public void registerMicroMaterials() {
        //FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(Electrometrics.blockElectricityMeter, 1, 0));
    }
}
