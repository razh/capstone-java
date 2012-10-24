package org.capstone.game;

import com.badlogic.gdx.math.Vector2;

public class Entity {
	protected Vector2 position = new Vector2();

	public Entity(float x, float y) {
		setX(x);
		setY(y);
	}

	public float getX() {
		return position.x;
	}

	public void setX(float x) {
		position.x = x;
	}

	public float getY() {
		return position.y;
	}

	public void setY(float y) {
		position.y = y;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 v) {
		position = v;
	}
}
