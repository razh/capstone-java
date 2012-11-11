package org.capstone.game.physics;

public class RectPhysicsComponent extends PhysicsComponent {
	private float width;
	private float height;

	public RectPhysicsComponent(float x, float y, float width, float height) {
		super(x, y);

		setWidth(width);
		setHeight(height);
	}

	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
