package org.halvors.electrometrics.common.util.location;

import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.halvors.electrometrics.common.tile.TileEntity;

/**
 * ChunkLocation - an integer-based way to keep track of and perform operations on chunks in a Minecraft-based environment. This also takes
 * in account the dimension the chunk is in.
 * @author aidancbrady
 *
 */
public class ChunkLocation {
	private final int dimensionId;
	private final int x;
	private final int z;

	/**
	 * Creates a ChunkLocation object from the given x and z coordinates, as well as a dimension.
	 * @param x - chunk x location
	 * @param z - chunk z location
	 * @param dimensionId - the dimension this ChunkLocation is in
	 */
	public ChunkLocation(int dimensionId, int x, int z) {
		this.dimensionId = dimensionId;
		this.x = x;
		this.z = z;
	}

	/**
	 * Creates a ChunkLocation from a BlockLocation based on it's coordinates and dimension.
	 * @param blockLocation - the BlockLocation object to get this ChunkLocation from
	 */
	public ChunkLocation(BlockLocation blockLocation) {
		this.dimensionId = blockLocation.getDimensionId();
		this.x = blockLocation.getX() >> 4;
		this.z = blockLocation.getZ() >> 4;
	}

	/**
	 * Creates a ChunkLocation from an entity based on it's location and dimension.
	 * @param entity - the entity to get the ChunkLocation object from
	 */
	public ChunkLocation(Entity entity) {
		this(new BlockLocation(entity));
	}

	public ChunkLocation(TileEntity tileEntity) {
		this(new BlockLocation(tileEntity));
	}

	/**
	 * Whether or not this chunk exists in the given world.
	 * @param world - the world to check in
	 * @return if the chunk exists
	 */
	public boolean exists(World world) {
		return world.getChunkProvider().chunkExists(x, z);
	}

	/**
	 * Gets a Chunk object corresponding to this ChunkLocation's coordinates.
	 * @param world - the world to get the Chunk object from
	 * @return the corresponding Chunk object
	 */
	public Chunk getChunk(World world) {
		return world.getChunkFromChunkCoords(x, z);
	}

	/**
	 * Returns this ChunkLocation in the Minecraft-based ChunkCoordIntPair format.
	 * @return this ChunkLocation as a ChunkCoordIntPair
	 */
	public ChunkCoordIntPair toPair() {
		return new ChunkCoordIntPair(x, z);
	}

	public int getDimensionId() {
		return dimensionId;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ChunkLocation) {
			ChunkLocation chunk = (ChunkLocation) object;

			return chunk.dimensionId == dimensionId &&
					chunk.x == x &&
					chunk.z == z;
		}

		return false;
	}
}