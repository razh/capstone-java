package org.capstone.game;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	private Vector2 position = new Vector2();

	public Entity(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	public float getX() {
		return this.position.x;
	}

	public void setX(float x) {
		this.position.x = x;
	}

	public float getY() {
		return this.position.y;
	}

	public void setY(float y) {
		this.position.y = y;
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void setPosition(Vector2 v) {
		this.position = v;
	}
}
