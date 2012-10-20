package org.capstone.game.physics;

public class RectPhysicsComponent extends PhysicsComponent {
	private float width;
	private float height;

	public RectPhysicsComponent(float x, float y, float width, float height) {
		super(x, y);

		this.width  = width;
		this.height = height;
	}

	@Override
	public void update(double elapsedTime) {
		super.update(elapsedTime);
	}
}
