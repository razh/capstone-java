package org.capstone.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player {
	private int score;
	private int level;
	private int team;
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

	public Player() {
		setScore(0);
		setLevel(0);
		setTeam(0);
	}
}
