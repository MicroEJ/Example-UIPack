/*
 * Java
 *
 * Copyright 2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example.ui.customfontlayout;

import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;

/**
 * Writes a character string using U+202E (RIGHT-TO-LEFT OVERRIDE) and U+202C (POP DIRECTIONAL FORMATTING).
 */
public class CustomFontLayoutDemo {

	/**
	 * Entry point.
	 *
	 * @param args
	 *            unused
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args) {
		MicroUI.start();

		Display display = Display.getDisplay();
		GraphicsContext gc = display.getGraphicsContext();

		Font font = Font.getDefaultFont();

		gc.setColor(Colors.BLACK);
		Painter.fillRectangle(gc, 0, 0, gc.getWidth(), gc.getHeight());

		gc.setColor(Colors.WHITE);
		Painter.drawString(gc, "Writing a character string without directional overrides", font, 20, 20);
		Painter.drawString(gc, "Writing \u202Ea character string\u202C with directional overrides", font, 20, 40);

		display.flush();
	}

}
