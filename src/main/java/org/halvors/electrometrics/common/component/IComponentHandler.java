package org.halvors.electrometrics.common.component;

public interface IComponentHandler {
    <Class extends IComponent> Class add(Class component);

    <Class extends IComponent> void remove(Class component);
}
