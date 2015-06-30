package org.halvors.electrometrics.common.component;

public interface IComponentHandler {
	<Component extends IComponent> Component add(Component component);

	<Component extends IComponent> void remove(Component component);
}
