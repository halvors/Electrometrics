package org.halvors.electrometrics.common.component;

import java.util.HashSet;
import java.util.Set;

public interface IComponentContainer {
    Set<IComponent> components = new HashSet<>();

    boolean hasComponent(Class<?> componentClass);

    IComponent getComponent(Class<?> componentClass);
}
