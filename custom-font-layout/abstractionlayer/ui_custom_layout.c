/*
 * C
 *
 * Copyright 2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

/**
 * @file ui_custom_layout.c
 *
 * This file contains an implementation of <code>UI_DRAWING_drawString</code>, which will be called instead of the default
 * implementation when drawing text.
 *
 * This function provides basic handling of the Unicode characters <code>RIGHT-TO-LEFT OVERRIDE</code> and <code>POP DIRECTIONAL
 * FORMATTING</code>, which allows interweaving right-to-left text with left-to-right text.
 *
 * Without this function, the default implementation would be called, which does not handle these characters and will
 * attempt to print them instead (therefore printing a fallback character).
 */

#include "LLUI_PAINTER_impl.h"
#include "ui_drawing_soft.h"
#include "ui_drawing.h"
#include "LLUI_DISPLAY.h"

#include <stddef.h>

/**
 * Constants to identify special characters that must be processed
 */
#define RTL_OVERRIDE 0x202E
#define POP_DIRECTIONAL_FORMATTING 0x202C

/**
 * Override for the default drawString function
 */
DRAWING_Status UI_DRAWING_drawString(MICROUI_GraphicsContext *gc, jchar *chars, jint length, MICROUI_Font *font, jint x,
                                     jint y) {
	// Stores the end horizontal coordinate of the current RTL segment. 0 when processing LTR text.
	size_t rtl_x = 0;

	for (size_t i = 0; i < length; i++) {
		if (chars[i] == RTL_OVERRIDE) {
			// Beginning of an RTL segment

			// Count the number of characters to draw in the segment.
			size_t rtl_count = 0;
			for (size_t j = i + 1; j < length && chars[j] != POP_DIRECTIONAL_FORMATTING; j++) {
				// Compute and store the end coordinate of the segment.
				rtl_count++;
			}

			// Compute and store the end coordinate of the segment.
			x += UI_DRAWING_SOFT_stringWidth(chars + i + 1, rtl_count, font);
			rtl_x = x;
		} else if (chars[i] == POP_DIRECTIONAL_FORMATTING) {
			// End of an RTL segment

			// Go to the stored end coordinate and reset it.
			x = rtl_x;
			rtl_x = 0;
		} else {
			// Other characters

			if (rtl_x != 0) {
				// When drawing an RTL segment, advance backwards before drawing the character.
				x -= UI_DRAWING_SOFT_stringWidth(chars + i, 1, font);
			}

			UI_DRAWING_SOFT_drawChar(gc, chars[i], font, x, y);

			if (rtl_x == 0) {
				// When drawing an LTR segment, advance forward after drawing the character
				x += UI_DRAWING_SOFT_stringWidth(chars + i, 1, font);
			}
		}
	}

	LLUI_DISPLAY_setDrawingStatus(DRAWING_DONE);
}
