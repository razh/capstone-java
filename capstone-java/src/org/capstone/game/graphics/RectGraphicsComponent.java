package org.capstone.game.graphics;

import com.badlogic.gdx.graphics.Color;

public class RectGraphicsComponent extends GraphicsComponent {
	private float width;
	private float height;

	public RectGraphicsComponent(float x, float y, Color color,
	                             float width, float height) {
		super(x, y, color);

		this.setWidth(width);
		this.setHeight(height);
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public void init() {

	}

	@Override
	public void render() {

	}
}
