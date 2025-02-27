/*
 * Kotlin
 *
 * Copyright 2024-2025 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

plugins {
	id("com.microej.gradle.application") version "1.0.0"
}

group = "com.microej.example.ui"
version = "1.0.0"

microej {
	applicationEntryPoint = "com.microej.example.ui.customfontlayout.CustomFontLayoutDemo"
}

dependencies {
	implementation("ej.api:edc:1.3.5")
	implementation("ej.api:microui:3.1.0")

	microejVee("XXX")
}
