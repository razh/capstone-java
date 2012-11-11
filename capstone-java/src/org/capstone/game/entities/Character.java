package org.capstone.game.entities;

import org.capstone.game.CircleMeshActor;

import com.badlogic.gdx.graphics.Color;

public class Character extends CircleMeshActor {
	protected int team = 0;

	public Character(float x, float y, Color color, float radius) {
		super();

		setPosition(x, y);
		setColor(color);
		setWidth(radius);
		setHeight(radius);
	}

	public void act(float elapsedTime) {
		super.act(elapsedTime);
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
}
