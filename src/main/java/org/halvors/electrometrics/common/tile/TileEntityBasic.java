package org.halvors.electrometrics.common.tile;

import net.minecraft.tileentity.TileEntity;

public class TileEntityBasic extends TileEntity {
	// The name of this TileEntity.
	private final String name;

	public TileEntityBasic(String name) {
		super();

		this.name = name;
	}

	public String getName() {
		return name;
	}
}
