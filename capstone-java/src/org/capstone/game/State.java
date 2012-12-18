package org.capstone.game;

import org.capstone.game.io.LevelLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/*
 * Bad design right here. A global state class? What are you thinking?
 */
public class State {
	private static float width;
	private static float height;

	private static Player player;
	private static LevelLoader loader;
	private static Level level;
	private static MeshStage stage;

	public static float EPSILON = 1E-10f;
	public static boolean debug = true;
	public static boolean debugRendering = false;
	
	public State() {
		setWidth(Gdx.graphics.getWidth());
		setHeight(Gdx.graphics.getHeight());
		setColor(Color.BLACK);
		
		stage = new MeshStage(width, height, true);
	}

	public State(float width, float height, Color color) {
		stage = new MeshStage(width, height, true);

		setWidth(width);
		setHeight(height);
		setColor(color);
	}

	public static void update() {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f);
		if (loader != null)
			loader.act(delta);

		if (level != null)
			level.act(delta);

		stage.act(delta);
		setColor(stage.getColor());
	}

	// Do not use these. Use Gdx.graphics.getWidth() and Gdx.graphics.getHeight() instead!
	public static float getWidth() {
		// return width;
		return 1280.0f;
	}

	public static void setWidth(float w) {
		width = w;
	}

	public static float getHeight() {
		// return height;
		return 720.0f;
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

	public static LevelLoader getLoader() {
		return loader;
	}

	public static void setLoader(LevelLoader loader) {
		State.loader = loader;
	}

	public static void setLevel(Level level) {
		State.level = level;
		level.setStage(getStage());
	}

	public static Level getLevel() {
		return level;
	}
}
