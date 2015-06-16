package org.halvors.ElectricityMeter.block;

import nova.core.block.Stateful;
import nova.core.block.component.StaticBlockRenderer;
import nova.core.entity.component.Player;
import nova.core.util.shape.Cuboid;
import org.halvors.ElectricityMeter.ElectricityMeter;
import org.halvors.ElectricityMeter.block.BlockBasic;

import java.util.Optional;

/**
 * This the solar panel block.
 *
 * @author halvors
 */
public class BlockElectricityMeter extends BlockBasic implements Stateful {
	//private ConnectedTextureRenderer renderer = add(new ConnectedTextureRenderer(this, ElectricityMeter.solarPanelTextureEdge));
	private StaticBlockRenderer renderer = add(new StaticBlockRenderer(this));

	public BlockElectricityMeter() {
		super();

		collider.setBoundingBox(new Cuboid(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F));
		collider.isOpaqueCube(false);

		renderer.setTexture((direction) -> Optional.of(ElectricityMeter.blockElectricityMeterTexture));

        /*
        renderer.setTexture((direction) -> {
            switch (direction) {
                case UP:
                    return Optional.of(ConcentratedSolars.solarPanelTextures.get(0));

                case DOWN:
                    return Optional.of(ConcentratedSolars.solarPanelTextures.get(2));

                default:
                    return Optional.of(ConcentratedSolars.solarPanelTextures.get(1));
            }
        });
        */

		events.on(RightClickEvent.class).bind(this::onRightClick);

        /*
        Not ported features.

		setHardness(3.5F);
		setResistance(8F);
		setStepSound(soundTypeMetal);
		*/
	}

	public void onRightClick(RightClickEvent event) {
		// Get the player specific component from entity.
		Player player = event.entity.get(Player.class);

		
	}

	/*
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int metadata, float what, float these,
			float are) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		player.openGui(ElectricityMeter.instance, 0, world, x, y, z);

		return true;
	}
	*/

	@Override
	public String getID() {
		return "blockElectricityMeter";
	}
}
