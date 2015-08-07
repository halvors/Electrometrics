package org.halvors.electrometrics.common.multipart.part;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JCuboidPart;
import codechicken.multipart.JIconHitEffects;
import codechicken.multipart.JNormalOcclusion;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.Reference;

public class PartMachine extends JCuboidPart implements JNormalOcclusion, JIconHitEffects {
	private static Cuboid6[] bounds = new Cuboid6[6];

	static {
		Cuboid6 cuboid = new Cuboid6(0.25, 0, 0.25, 0.75, 0.125, 0.75);
		Translation fromOrigin = new Translation(Vector3.center);
		Translation toOrigin = (Translation)fromOrigin.inverse();

		for (int i = 0; i < 6; i++) {
			bounds[i] = cuboid.copy().apply(toOrigin).apply(Rotation.sideRotations[i]).apply(fromOrigin);
		}
	}

	private ForgeDirection side = ForgeDirection.DOWN;

	public PartMachine() {
		super();
	}

	@Override
	public String getType() {
		return Reference.PREFIX + "machine";
	}

	@Override
	public IIcon getBreakingIcon(Object subPart, int side) {
		return null;
	}

	@Override
	public IIcon getBrokenIcon(int side) {
		return null;
	}

	@Override
	public Iterable<Cuboid6> getOcclusionBoxes() {
		return null;
	}

	@Override
	public Cuboid6 getBounds() {
		return bounds[side.ordinal()];
	}

	/*
	@Override
	public Cuboid6 getBounds() {
		return bounds[side.ordinal()];
	}

	@Override
	public String getType() {
		return "mekanism:glow_panel";
	}

	@Override
	public void onNeighborChanged()
	{
		if(!world().isRemote && !canStay())
		{
			TileMultipart.dropItem(new ItemStack(MekanismItems.GlowPanel, 1, colour.getMetaValue()), world(), Vector3.fromTileEntityCenter(tile()));
			tile().remPart(this);
		}
	}

	@Override
	public void onPartChanged(TMultiPart other)
	{
		if(!world().isRemote && !canStay())
		{
			TileMultipart.dropItem(new ItemStack(MekanismItems.GlowPanel, 1, colour.getMetaValue()), world(), Vector3.fromTileEntityCenter(tile()));
			tile().remPart(this);
		}
	}

	@Override
	public void writeDesc(MCDataOutput data) {
		data.writeInt(side.ordinal());
		data.writeInt(color.getMeta());
	}

	@Override
	public void readDesc(MCDataInput data) {
		side = ForgeDirection.getOrientation(data.readInt());
		color = Color.values()[data.readInt()];
	}

	@Override
	public void save(NBTTagCompound nbt) {
		nbt.setInteger("side", side.ordinal());
		nbt.setInteger("colour", color.getMeta());
	}

	@Override
	public void load(NBTTagCompound nbt) {
		side = ForgeDirection.getOrientation(nbt.getInteger("side"));
		color = Color.values()[nbt.getInteger("colour")];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderStatic(Vector3 pos, int pass) {
		if (pass == 0) {
			RenderGlowPanel.getInstance().renderStatic(this);
			return true;
		}

		return false;
	}

	@Override
	public int getLightValue() {
		return 15;
	}

	@Override
	public Iterable<Cuboid6> getOcclusionBoxes() {
		return getCollisionBoxes();
	}

	@Override
	public boolean occlusionTest(TMultiPart other) {
		return NormalOcclusionTest.apply(this, other);
	}

	@Override
	public IIcon getBreakingIcon(Object subPart, int side) {
		return RenderGlowPanel.icon;
	}

	@Override
	public IIcon getBrokenIcon(int side) {
		return RenderGlowPanel.icon;
	}

	@Override
	public void addHitEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
		IconHitEffects.addHitEffects(this, hit, effectRenderer);
	}

	@Override
	public void addDestroyEffects(MovingObjectPosition mop, EffectRenderer effectRenderer) {
		IconHitEffects.addDestroyEffects(this, effectRenderer, false);
	}

	@Override
	public Iterable<ItemStack> getDrops() {
		return Collections.singletonList(pickItem(null));
	}

	@Override
	public ItemStack pickItem(MovingObjectPosition hit)
	{
		return new ItemStack(MekanismItems.GlowPanel, 1, color.getMeta());
	}

	@Override
	public boolean doesTick() {
		return false;
	}

	public boolean canStay() {
		TileMultipart tile = tile();

		return world().isSideSolid(tile.xCoord,tile.yCoord, tile.zCoord, side.getOpposite()) || tile().partMap(side.ordinal()) instanceof HollowMicroblock;
	}
	*/
}