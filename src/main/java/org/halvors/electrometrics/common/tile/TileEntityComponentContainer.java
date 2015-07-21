package org.halvors.electrometrics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.component.IComponent;
import org.halvors.electrometrics.common.tile.component.ITileComponent;
import org.halvors.electrometrics.common.tile.component.ITileNetworkableComponent;

import java.util.ArrayList;
import java.util.List;

public class TileEntityComponentContainer extends TileEntity implements ITileNetworkable {
	protected final List<IComponent> components = new ArrayList<>();

	TileEntityComponentContainer(String inventoryName) {
		super(inventoryName);
	}

	public List<IComponent> getComponents() {
		return components;
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

	public void onNeighborChange() {
		for (IComponent component : components) {
			if (component instanceof ITileComponent) {
				ITileComponent tileComponent = (ITileComponent) component;

				tileComponent.onNeighborChange();
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
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		ByteBuf tileComponentDataStream = dataStream;

		for (IComponent component : components) {
			if (component instanceof ITileNetworkableComponent) {
				ITileNetworkableComponent tileComponent = (ITileNetworkableComponent) component;

				tileComponentDataStream = tileComponent.handlePacketData(tileComponentDataStream);
			}
		}
	}

	@Override
	public List<Object> getPacketData(List<Object> list) {
		for (IComponent component : components) {
			if (component instanceof ITileNetworkableComponent) {
				ITileNetworkableComponent tileComponent = (ITileNetworkableComponent) component;

				list.addAll(tileComponent.getPacketData(new ArrayList<>()));
			}
		}

		return list;
	}
}
