package org.capstone.game.physics;

import org.capstone.game.State;

public class CirclePhysicsComponent extends PhysicsComponent {
	private float radius;

	public CirclePhysicsComponent(float x, float y, float radius) {
		super(x, y);

		this.setRadius(radius);
	}

	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		
		if (-State.getWidth() / 2 > this.getX() - this.getRadius()) {
			this.setX(this.getRadius() - State.getWidth() / 2);
			this.velocity.x = -this.velocity.x;
		}
		if (this.getX() + this.getRadius() > State.getWidth() / 2) {
			this.setX(State.getWidth() / 2 - this.getRadius());
			this.velocity.x = -this.velocity.x;
		}
		if (-State.getHeight() / 2 > this.getY() - this.getRadius()) {
			this.setY(this.getRadius() - State.getHeight() / 2);
			this.velocity.y = -this.velocity.y;
		}
		if (this.getY() + this.getRadius() > State.getHeight() / 2) {
			this.setY(State.getHeight() / 2 - this.getRadius());
			this.velocity.y = -this.velocity.y;
		}
	}

	public float getRadius() {
		return this.radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
