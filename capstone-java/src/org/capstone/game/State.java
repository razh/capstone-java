package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/*
 * Bad design right here. A global state class? What are you thinking?
 */
public class State {
	private static float width;
	private static float height;

	private static Player player;
	private static Level level;
	private static MeshStage stage;

	public static float EPSILON = 1E-10f;
	public static boolean debug = true;
	public static boolean debugRendering = false;

	public State(float width, float height, Color color) {
		stage = new MeshStage(width, height, true);

		setWidth(width);
		setHeight(height);
		setColor(color);
	}

	public static void update() {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f);
		if (level != null)
			level.act(delta);

		stage.act(delta);
		setColor(stage.getColor());
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
		return stage.getColor();
	}

	public static void setColor(Color color) {
		stage.setColor(color);
	}

	public static Player getPlayer() {
		return player;
	}

	public static void setPlayer(Player player) {
		State.player = player;
	}

	public static MeshStage getStage() {
		return stage;
	}

	public static void setLevel(Level level) {
		State.level = level;
		level.setStage(getStage());
	}
}
