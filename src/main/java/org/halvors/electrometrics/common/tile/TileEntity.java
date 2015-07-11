package org.halvors.electrometrics.common.tile;

import net.minecraft.world.IBlockAccess;

public class TileEntity extends net.minecraft.tileentity.TileEntity {
	// The name of this TileEntity.
	private final String name;

	TileEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static TileEntity getTileEntity(IBlockAccess world, int x, int y, int z) {
		return (TileEntity) world.getTileEntity(x, y, z);
	}
}
