package org.halvors.electrometrics.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.base.ResourceType;

@SideOnly(Side.CLIENT)
public class BlockRenderer {
	private static final String[] sides = new String[] { "Bottom", "Top", "Front", "Back", "Left", "Right" };

	public static void loadDynamicTextures(IIconRegister iconRegister, String name, IIcon[] textures, DefaultIcon... defaultIcons) {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			String texture = name + sides[side.ordinal()];
			String textureActive = texture + "Active";

			if (textureExists(texture)) {
				textures[side.ordinal()] = iconRegister.registerIcon(Reference.PREFIX + texture);

				if (textureExists(textureActive)) {
					textures[side.ordinal() + 6] = iconRegister.registerIcon(Reference.PREFIX + textureActive);
				} else {
					boolean found = false;

					for (DefaultIcon defaultIcon : defaultIcons) {
						if (defaultIcon.getIcons().contains(side.ordinal() + 6)) {
							textures[side.ordinal() + 6] = defaultIcon.getDefaultIcon();
							found = true;
						}
					}

					if (!found) {
						textures[side.ordinal() + 6] = iconRegister.registerIcon(Reference.PREFIX + texture);
					}
				}
			} else {
				for (DefaultIcon defaultIcon : defaultIcons) {
					if (defaultIcon.getIcons().contains(side.ordinal())) {
						textures[side.ordinal()] = defaultIcon.getDefaultIcon();
					}

					if (defaultIcon.getIcons().contains(side.ordinal() + 6)) {
						textures[side.ordinal() + 6] = defaultIcon.getDefaultIcon();
					}
				}
			}
		}
	}

	private static boolean textureExists(String fileName) {
		ResourceLocation resourceLocation = new ResourceLocation(Reference.DOMAIN, ResourceType.TEXTURE_BLOCKS.getPrefix() + fileName + ".png");

		try {
			Minecraft.getMinecraft().getResourceManager().getAllResources(resourceLocation);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}