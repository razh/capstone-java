package org.capstone.game;

import com.badlogic.gdx.Gdx;

public class State {
	private static float width;
	private static float height;

	private MeshStage stage;

	public State(float width, float height) {
		stage = new MeshStage(width, height, true);

		setWidth(width);
		setHeight(height);
	}

	public void update() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f));
	}

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

	public MeshStage getStage() {
		return stage;
	}
}
