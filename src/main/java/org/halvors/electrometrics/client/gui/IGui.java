package org.halvors.electrometrics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IIcon;
import org.halvors.electrometrics.common.util.LanguageUtils;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IGui {
	/**
	 * Draws a textured rectangle in this GUI.
	 * @param x
	 * @param y
	 * @param u
	 * @param v
	 * @param w
	 * @param h
	 */
	void drawTexturedRect(int x, int y, int u, int v, int w, int h);

	/**
	 * Draws a textured rectangle from the specified icon.
	 * @param x
	 * @param y
	 * @param icon
	 * @param w
	 * @param h
	 */
	void drawTexturedRectFromIcon(int x, int y, IIcon icon, int w, int h);

	void drawString(String text, int x, int y);

	/**
	 * Display the specified string as tooltip at the specified location.
	 * @param s
	 * @param xAxis
	 * @param yAxis
	 */
	void displayTooltip(String s, int xAxis, int yAxis);

	/**
	 * Display a list of tooltips at the specified location.
	 * @param list
	 * @param xAxis
	 * @param yAxis
	 */
	void displayTooltips(List<String> list, int xAxis, int yAxis);

	/**
	 * The fontrenderer object of this GUI.
	 * @return FontRenderer
	 */
	FontRenderer getFontRenderer();
}

