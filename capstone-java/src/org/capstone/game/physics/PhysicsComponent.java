package org.capstone.game.physics;

import org.capstone.game.Entity;

import com.badlogic.gdx.math.Vector2;

public abstract class PhysicsComponent extends Entity {
	private Vector2 velocity;
	
	public PhysicsComponent(float x, float y) {
		super(x, y);
		
		this.setVelocity(new Vector2(0.0f, 0.0f));
	}

	public void update(double elapsedTime) {
		 
	}

	public Vector2 getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
}
