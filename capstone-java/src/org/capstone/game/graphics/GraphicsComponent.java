package org.capstone.game.graphics;

import org.capstone.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class GraphicsComponent extends Entity {
	protected Color color;

	public GraphicsComponent(float x, float y, Color color) {
		super(x, y);

		this.color = new Color();
		this.setColor(color);
	}

	public abstract void init();

	public abstract void render(ShaderProgram shaderProgram);

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color.set(color);
	}

	public float getRed() {
		return this.color.r;
	}

	public void setRed(float r) {
		this.color.r = r;
	}

	public float getGreen() {
		return this.color.g;
	}

	public void setGreen(float g) {
		this.color.g = g;
	}

	public float getBlue() {
		return this.color.b;
	}

	public void setBlue(float b) {
		this.color.b = b;
	}

	public float getAlpha() {
		return this.color.a;
	}

	public void setAlpha(float a) {
		this.color.a = a;
	}
}
