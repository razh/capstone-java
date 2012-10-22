package org.capstone.game;

import org.capstone.game.entities.Character;

import java.util.ArrayList;

public class State {
	private long prevTime;
	private long currTime;
	
	private static float width;
	private static float height;

	public State(float width, float height) {
		this.prevTime = System.nanoTime();
		this.currTime = prevTime;
		
		State.width  = width;
		State.height = height;
	}

	private ArrayList<Character> characters = new ArrayList<Character>();

	public void addCharacter(Character character) {
		this.characters.add(character);
	}

	public void removeCharacter(Character character) {
		this.characters.remove(character);
	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	public void update() {
		this.currTime = System.nanoTime();
		long elapsedTime = this.currTime - this.prevTime;
		this.prevTime = this.currTime;

		for (int i = 0; i < this.characters.size(); i++) {
			this.characters.get(i).update(elapsedTime);
		}
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
}
