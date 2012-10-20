package org.capstone.game.physics;

import org.capstone.game.Entity;

public abstract class PhysicsComponent extends Entity {
	public PhysicsComponent(float x, float y) {
		super(x, y);
	}

	public abstract void update(double elapsedTime); 
}
