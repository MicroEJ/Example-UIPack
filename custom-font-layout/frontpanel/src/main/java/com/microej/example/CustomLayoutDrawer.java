/*
 * Java
 *
 * Copyright 2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example;

import ej.microui.display.DisplayDrawer;
import ej.microui.display.MicroUIFont;
import ej.microui.display.MicroUIGraphicsContext;

/**
 * This class contains an override for <code>DisplayDrawer.drawString</code>, which will be called instead of the base
 * implementation when drawing text.
 *
 * This function provides basic handling of the Unicode characters <code>RIGHT-TO-LEFT OVERRIDE</code> and
 * <code>POP DIRECTIONAL FORMATTING</code>, which allows interweaving right-to-left text with left-to-right text.
 *
 * Without this function, the default implementation would be called, which does not handle these characters and will
 * attempt to print them instead (therefore printing a fallback character).
 */
public class CustomLayoutDrawer extends DisplayDrawer {
	/**
	 * Override for the default drawString function.
	 */
	@Override
	public void drawString(MicroUIGraphicsContext gc, char[] string, MicroUIFont font, int x, int y) {
		/**
		 * Constants to identify special characters that must be processed
		 */
		final int RTL_OVERRIDE = 0x202E;
		final int POP_DIRECTIONAL_FORMATTING = 0x202C;

		// Stores the end horizontal coordinate of the current RTL segment. 0 when
		// processing LTR text.
		int rtlX = 0;

		for (int i = 0; i < string.length; i++) {
			if (string[i] == RTL_OVERRIDE) {
				// Beginning of an RTL segment

				// Count the number of characters to draw in the segment.
				int rtlCount = 0;
				for (int j = i + 1; j < string.length && string[j] != POP_DIRECTIONAL_FORMATTING; j++) {
					rtlCount++;
				}

				// Compute and store the end coordinate of the segment.
				x += stringWidth(subarray(string, i + 1, rtlCount), font);
				rtlX = x;
			} else if (string[i] == POP_DIRECTIONAL_FORMATTING) {
				// End of an RTL segment

				// Go to the stored end coordinate and reset it.
				x = rtlX;
				rtlX = 0;
			} else {
				// Other characters

				if (rtlX != 0) {
					// When drawing an RTL segment, advance backwards before drawing the character.
					x -= stringWidth(subarray(string, i, 1), font);
				}
				drawChar(gc, string[i], font, x, y);
				if (rtlX == 0) {
					// When drawing an LTR segment, advance forward after drawing the character
					x += stringWidth(subarray(string, i, 1), font);
				}
			}
		}
	}

	/**
	 * Returns an array containing a subpart of the input array.
	 *
	 * @param array
	 *            the input array
	 * @param from
	 *            the index from which to start
	 * @param length
	 *            the number of elements to take
	 * @return an array containing the elements starting from the index <code>from</code> with <code>length</code>
	 *         elements
	 */
	public static char[] subarray(char[] array, int from, int length) {
		char[] output = new char[length];
		for (int i = 0; i < output.length; i++) {
			output[i] = array[from + i];
		}
		return output;
	}
}
