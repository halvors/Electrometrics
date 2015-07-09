package org.halvors.electrometrics.common.tile;

public class TileEntity extends net.minecraft.tileentity.TileEntity {
	// The name of this TileEntity.
	private String name;

	public TileEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
