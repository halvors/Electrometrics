package org.halvors.ElectricityMeter.gui;

import nova.core.gui.Gui;
import nova.core.gui.component.Button;
import nova.core.gui.component.Container;
import nova.core.gui.component.Label;
import nova.core.gui.component.inventory.PlayerInventory;
import nova.core.gui.layout.Anchor;
import nova.core.gui.layout.FlowLayout;

/**
 * Created by Halvor on 01.06.2015.
 */
public class GuiBasic extends Gui {
    public GuiBasic() {
        super("guiBasic"); // TODO: Pass variable from constructor.

        add(new Label("title", "Fortron Capacitor"), Anchor.NORTH);

        add(new Container("layout")
                .add(new Container("north")
                                .setLayout(new FlowLayout())
                                .add(new Label("linkedDevices", " ")).setMinimumSize(200, 20)
                                .add(new Label("transmissionRate", " ")).setMinimumSize(200, 20), Anchor.NORTH)
                .add(new Container("south")
                        .setLayout(new FlowLayout())
                        .add(new Button("toggle", "Toggle Mode")
                                        .setPreferredSize(80, 20)), Anchor.SOUTH), Anchor.CENTER);

        add(new PlayerInventory("inventory"), Anchor.SOUTH);
    }
}
