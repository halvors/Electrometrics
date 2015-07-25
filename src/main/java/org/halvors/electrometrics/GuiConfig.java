package org.halvors.electrometrics;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.halvors.electrometrics.common.Reference;

public class GuiConfig extends cpw.mods.fml.client.config.GuiConfig {
    public GuiConfig(GuiScreen parent) {
        super(parent,
                new ConfigElement(Electrometrics.getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Reference.ID,
                false,
                false,
                "Play Magic Beans Any Way You Want");
        //titleLine2 = MagicBeans.configFile.getAbsolutePath();
    }

    @Override
    public void initGui() {
        // You can add buttons and initialize fields here
        super.initGui();
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // You can do things like create animations, draw additional elements, etc. here
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // You can process any additional buttons you may have added here
        super.actionPerformed(button);
    }
}