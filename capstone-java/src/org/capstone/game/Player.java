package org.capstone.game;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player {
	private int score = 0;
	private int level = 0;
	private Actor selected = null;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public Actor getSelected() {
		return this.selected;
	}
	
	public boolean isSelected() {
		return this.selected != null;
	}
	
	public void setSelected(Actor actor) {
		this.selected = actor;
	}
	
	public Player() {
		
	}
}
