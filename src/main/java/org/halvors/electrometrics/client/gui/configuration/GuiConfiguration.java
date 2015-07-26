package org.halvors.electrometrics.client.gui.configuration;

import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import org.halvors.electrometrics.client.gui.configuration.category.CategoryEntryClient;
import org.halvors.electrometrics.client.gui.configuration.category.CategoryEntryGeneral;
import org.halvors.electrometrics.client.gui.configuration.category.CategoryEntryIntegration;
import org.halvors.electrometrics.client.gui.configuration.category.CategoryEntryMachine;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.util.LanguageUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiConfiguration extends GuiConfig {
    static final List<IConfigElement> configElements = new ArrayList<>();

    static {
        configElements.add(new DummyCategoryElement(LanguageUtils.localize("gui.config.category.general"), "gui.config.category.general", CategoryEntryGeneral.class));
        configElements.add(new DummyCategoryElement(LanguageUtils.localize("gui.config.category.machine"), "gui.config.category.machine", CategoryEntryMachine.class));
        configElements.add(new DummyCategoryElement(LanguageUtils.localize("gui.config.category.integration"), "gui.config.category.integration", CategoryEntryIntegration.class));
        configElements.add(new DummyCategoryElement(LanguageUtils.localize("gui.config.category.client"), "gui.config.category.client", CategoryEntryClient.class));
    }

    public GuiConfiguration(GuiScreen parent) {
        super(parent, configElements, Reference.ID, false, false, Reference.NAME);
    }
}