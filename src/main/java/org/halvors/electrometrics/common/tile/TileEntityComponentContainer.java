package org.halvors.electrometrics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.component.IComponent;
import org.halvors.electrometrics.common.component.IComponentContainer;
import org.halvors.electrometrics.common.tile.component.ITileComponent;

import java.util.ArrayList;
import java.util.List;

public class TileEntityComponentContainer extends TileEntity implements IComponentContainer, ITileNetworkable {
	protected TileEntityComponentContainer(String inventoryName) {
		super(inventoryName);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				tileComponent.onUpdate();
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				tileComponent.readFromNBT(nbtTagCompound);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				tileComponent.writeToNBT(nbtTagCompound);
			}
		}
	}

	@Override
	public boolean hasComponent(Class<?> componentClass) {
		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				return tileComponent.getClass().isInstance(componentClass);
			}
		}

		return false;
	}

	@Override
	public IComponent getComponent(Class<?> componentClass) {
		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				if (tileComponent.getClass().isInstance(componentClass)) {
					return tileComponent;
				}
			}
		}

		return null;
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		for (IComponent component : components) {
			if (component instanceof ITileComponent && component instanceof ITileNetworkable) {
				ITileNetworkable tileComponent = (ITileNetworkable) component;

				tileComponent.handlePacketData(dataStream);
			}
		}
	}

	@Override
	public List<Object> getPacketData(List<Object> list) {
		for (IComponent component : components) {
			if (component instanceof ITileComponent && component instanceof ITileNetworkable) {
				ITileNetworkable tileComponent = (ITileNetworkable) component;

				list.addAll(tileComponent.getPacketData(new ArrayList<>()));
			}
		}

		return list;
	}

	public void onNeighborChange() {
		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				tileComponent.onNeighborChange();
			}
		}
	}
}
