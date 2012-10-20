package org.capstone.game.graphics;

import com.badlogic.gdx.graphics.Color;

public class CircleGraphicsComponent extends GraphicsComponent {
	private float radius;
	
	public CircleGraphicsComponent(float x, float y, Color color, float radius) {
		super(x, y, color);
		
		this.setRadius(radius);
	}

	public float getRadius() {
		return this.radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public void render() {
	
	}
}
