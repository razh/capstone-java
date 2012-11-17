package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class State {
	private static float width;
	private static float height;
	private static Color color;

	private static MeshStage stage;

	public static boolean debug = true;
	public static boolean debugRendering = false;

	public State(float width, float height, Color color) {
		stage = new MeshStage(width, height, true);

		setWidth(width);
		setHeight(height);
		setColor(color);
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

	public static Color getColor() {
		return color;
	}

	public static void setColor(Color color) {
		State.color = color;
	}

	public static MeshStage getStage() {
		return stage;
	}
}
