package org.halvors.electrometrics.common.updater;

import org.apache.logging.log4j.Logger;

public interface IUpdatableMod {
    String getModId();

    String getModName();

    String getModVersion();

    Logger getStaticLogger();
}
