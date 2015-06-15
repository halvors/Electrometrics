package org.halvors.ElectricityMeter.gui;

import nova.core.gui.Background;
import nova.core.gui.ComponentEvent;
import nova.core.gui.Gui;
import nova.core.gui.GuiEvent;
import nova.core.gui.component.Button;
import nova.core.gui.component.Container;
import nova.core.gui.component.Label;
import nova.core.gui.component.inventory.Slot;
import nova.core.gui.layout.Anchor;
import nova.core.gui.layout.FlowLayout;
import nova.core.network.NetworkTarget;
import nova.core.render.Color;

public class GuiElectricityMeter extends Gui {
    public GuiElectricityMeter() {
        super("guiElectricityMeter");

        Button button = new Button("testbutton2", "I'm EAST");
        button.setMaximumSize(Integer.MAX_VALUE, 120);

        button.onEvent((event, component) -> {
            System.out.println("Test button pressed! " + NetworkTarget.Side.get());
        }, ComponentEvent.ActionEvent.class, NetworkTarget.Side.BOTH);

        Button button1 = new Button("testbutton3", "I'm CENTER");

        Container container = new Container("test");
        container.setLayout(new FlowLayout());
        container.add(new Slot("main", 0));
        container.add(new Slot("main", 0));
        container.add(new Slot("main", 0));
        container.add(new Slot("main", 0));

        Container container1 = new Container("container");
        container1.setLayout(new FlowLayout());
        container1.add(new Button("testbutton5", "I'm the FIRST Button and need lots of space"));

        Label label = new Label("testlabel1", "I'm some label hanging around");
        label.setBackground(new Background(Color.white));
        container1.add(label);

        container1.add(new Button("testbutton7", "I'm THIRD"));
        container1.add(new Button("testbutton8", "I'm FOURTH"));

        container1.onGuiEvent((event) -> {
            //event.org.halvors.ElectricityMeter.gui.addInventory("main", ((BlockElectricityMeter) event.block.get()).inventory);
            System.out.println("Test GUI initialized! " + event.player.getDisplayName() + " " + event.position);
        }, GuiEvent.BindEvent.class);

        container1.onGuiEvent((event) -> {
            System.out.println("Test GUI closed!");
        }, GuiEvent.UnBindEvent.class);

        // Add the components.
        add(button, Anchor.EAST);
        add(button1);
        add(container, Anchor.SOUTH);
        add(container1, Anchor.NORTH);
    }
}