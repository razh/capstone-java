package org.capstone.game.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;

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
	public void init() {
		this.mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject, true, 0, 0, null);
	}

	@Override
	public void render() {
	
	}
}
