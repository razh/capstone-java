package org.capstone.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "capstone-java";
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 480;
		cfg.samples = 4;
		cfg.useCPUSynch = false;
		
		new LwjglApplication(new Game(), cfg);
	}
}
