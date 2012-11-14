package org.capstone.game;

import com.badlogic.gdx.Gdx;

public class State {
	private static float width;
	private static float height;

	private static MeshStage stage;

	public static boolean debug = true;
	public static boolean debugRendering = false;

	public State(float width, float height) {
		stage = new MeshStage(width, height, true);

		setWidth(width);
		setHeight(height);
	}

	public static void update() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f));
	}

	// Do not use these. Use Gdx.graphics.getWidth() and Gdx.graphics.getHeight() instead!
	public static float getWidth() {
		return width;
	}

	public static void setWidth(float w) {
		width = w;
	}

	public static float getHeight() {
		return height;
	}

	public static void setHeight(float h) {
		height = h;
	}

	public static MeshStage getStage() {
		return stage;
	}
}
