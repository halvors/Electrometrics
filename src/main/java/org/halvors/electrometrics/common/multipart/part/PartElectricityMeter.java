package org.halvors.electrometrics.common.multipart.part;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;

public class PartElectricityMeter extends TMultiPart implements TSlottedPart {
    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getSlotMask() {
        return 0;
    }
}
