package org.capstone.game.physics;

public class CirclePhysicsComponent extends PhysicsComponent {
	private float radius;

	public CirclePhysicsComponent(float x, float y, float radius) {
		super(x, y);

		this.radius = radius;
	}

	@Override
	public void update(double elapsedTime) {

	}

}
