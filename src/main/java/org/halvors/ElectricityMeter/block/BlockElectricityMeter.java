package org.halvors.ElectricityMeter.block;

import nova.core.block.Stateful;
import nova.core.block.component.StaticBlockRenderer;
import nova.core.component.Passthrough;
import nova.core.util.shape.Cuboid;
import org.halvors.ElectricityMeter.ElectricityMeter;

import java.util.Optional;

/**
 * This the solar panel block.
 *
 * @author halvors
 */
@Passthrough("mekanism.api.energy.IStrictEnergyAcceptor")
public class BlockElectricityMeter extends BlockBasic implements Stateful {
    //private ConnectedTextureRenderer renderer = add(new ConnectedTextureRenderer(this, ConcentratedSolars.solarPanelTextureEdge));
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
        game.guiManager.showGui("guiBasic",  event.entity, position());

        game.networkManager.sync(this);
    }

    @Override
    public String getID() {
        return "blockElectricityMeter";
    }
}