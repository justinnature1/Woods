package com.woods.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.woods.game.Woods;

/**
 * Run this class to run game in Desktop mode.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 50;
		config.width = 50;
		config.fullscreen = true;
		new LwjglApplication(new Woods(), config);
	}
}