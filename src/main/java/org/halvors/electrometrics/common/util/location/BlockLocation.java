package org.halvors.electrometrics.common.util.location;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import org.halvors.electrometrics.common.tile.TileEntity;

public class BlockLocation {
	private final int dimensionId;
	private final int x;
	private final int y;
	private final int z;

	public BlockLocation(int dimensionId, int x, int y, int z) {
		this.dimensionId = dimensionId;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockLocation(Entity entity) {
		this.dimensionId = entity.dimension;
		this.x = entity.serverPosX;
		this.y = entity.serverPosY;
		this.z = entity.serverPosZ;
	}

	public BlockLocation(TileEntity tileEntity) {
		this.dimensionId = 0; //tileEntity.getWorldObj().provider.dimensionId;
		this.x = tileEntity.xCoord;
		this.y = tileEntity.yCoord;
		this.z = tileEntity.zCoord;
	}

	public int getDimensionId() {
		return dimensionId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Block getBlock(IBlockAccess world) {
		return world.getBlock(x, y, z);
	}

	public TileEntity getTileEntity(IBlockAccess world) {
		return (TileEntity) world.getTileEntity(x, y, z);
	}
}
