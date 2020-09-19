package com.andedit.badtrucks.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.andedit.badtrucks.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	public static void main(String[] args) {
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new Main(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.enableGLDebugOutput(true, System.err);
		config.disableAudio(true);
		config.setTitle("bad-trucks");
		config.setWindowedMode(1200, 900); // 800 600
		config.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return config;
	}
}