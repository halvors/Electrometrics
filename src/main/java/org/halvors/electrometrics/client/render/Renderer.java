package org.halvors.electrometrics.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.Reference;

@SideOnly(Side.CLIENT)
public class Renderer {
	private static final String[] sides = new String[] { "Bottom", "Top", "Front", "Back", "Left", "Right" };

	public static void loadDynamicTextures(IIconRegister iconRegister, String name, IIcon[] textures, DefaultIcon... defaults) {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			String texture = name + sides[side.ordinal()];
			String textureActive = texture + "Active";

			if (blockTextureExists(texture)) {
				textures[side.ordinal()] = iconRegister.registerIcon(Reference.PREFIX + texture);

				if (blockTextureExists(textureActive)) {
					textures[side.ordinal() + 6] = iconRegister.registerIcon(Reference.PREFIX + textureActive);
				} else {
					boolean found = false;

					for (DefaultIcon defaultIcon : defaults) {
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
				for (DefaultIcon defaultIcon : defaults) {
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

	private static boolean blockTextureExists(String texture) {
		String path = Reference.PREFIX + "textures/blocks/" + texture + ".png"; // Need to add :textures/blocks/?

		try {
			Minecraft.getMinecraft().getResourceManager().getAllResources(new ResourceLocation(path));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}