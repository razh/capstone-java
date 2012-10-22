package org.capstone.game;

import org.capstone.game.entities.Character;

import java.util.ArrayList;

public class State {
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
}
