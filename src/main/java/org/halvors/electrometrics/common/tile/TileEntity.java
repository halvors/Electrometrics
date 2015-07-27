package org.halvors.electrometrics.common.tile;

import net.minecraft.world.IBlockAccess;

public class TileEntity extends net.minecraft.tileentity.TileEntity {
	private final String inventoryName;

	protected TileEntity(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public net.minecraft.tileentity.TileEntity getNative() {
		return this;
	}

	public static TileEntity getTileEntity(IBlockAccess world, int x, int y, int z) {
		return (TileEntity) world.getTileEntity(x, y, z);
	}
}
