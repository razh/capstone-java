package org.capstone.game;

import org.capstone.game.entities.Character;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class State {
	private long prevTime;
	private long currTime;

	private static float width;
	private static float height;

	private MeshStage stage;

	public State(float width, float height) {
		stage = new MeshStage(width, height, true);
		
		prevTime = System.nanoTime();
		currTime = prevTime;

		setWidth(width);
		setHeight(height);
	}

	private ArrayList<Character> characters = new ArrayList<Character>();

	public void addCharacter(Character character) {
		characters.add(character);
	}

	public void removeCharacter(Character character) {
		characters.remove(character);
	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	public void update() {
		currTime = TimeUtils.millis();
		long elapsedTime = currTime - prevTime;
		prevTime = currTime;

		stage.act(elapsedTime);
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
