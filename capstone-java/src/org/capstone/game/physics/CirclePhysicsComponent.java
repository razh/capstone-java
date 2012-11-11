package org.capstone.game.physics;

import org.capstone.game.State;

public class CirclePhysicsComponent extends PhysicsComponent {
	private float radius;

	public CirclePhysicsComponent(float x, float y, float radius) {
		super(x, y);

		setRadius(radius);
	}

	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);

		if (-State.getWidth() / 2 > getX() - getRadius()) {
			setX(getRadius() - State.getWidth() / 2);
			velocity.x = -velocity.x;
		}
		if (getX() + getRadius() > State.getWidth() / 2) {
			setX(State.getWidth() / 2 - getRadius());
			velocity.x = -velocity.x;
		}
		if (-State.getHeight() / 2 > getY() - getRadius()) {
			setY(getRadius() - State.getHeight() / 2);
			velocity.y = -velocity.y;
		}
		if (getY() + getRadius() > State.getHeight() / 2) {
			setY(State.getHeight() / 2 - getRadius());
			velocity.y = -velocity.y;
		}
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
