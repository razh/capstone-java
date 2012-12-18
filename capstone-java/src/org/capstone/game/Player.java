package org.capstone.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player {
	private int score;
	private int level;
	private int team;
	private int health;
	private Actor selected;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
	
	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void changeHealth(int difference) {
		this.health += difference;
	}

	public Player() {
		setScore(0);
		setLevel(0);
		setTeam(0);
		setHealth(1);
	}
}
