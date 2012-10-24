package org.capstone.game.physics;

import org.capstone.game.Entity;

import com.badlogic.gdx.math.Vector2;

public abstract class PhysicsComponent extends Entity {
	protected Vector2 velocity;

	public PhysicsComponent(float x, float y) {
		super(x, y);

		setVelocity(0.0f, 0.0f);
	}

	public void update(long elapsedTime) {
		position.add(velocity.x * elapsedTime, velocity.y * elapsedTime);
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(float vx, float vy) {
		velocity = new Vector2(vx, vy);
	}
}
