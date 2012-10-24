package org.capstone.game.graphics;

import org.capstone.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class GraphicsComponent extends Entity {
	protected Color color;

	public GraphicsComponent(float x, float y, Color color) {
		super(x, y);

		setColor(color);
	}

	public abstract void init();

	public abstract void render(ShaderProgram shaderProgram);

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getRed() {
		return color.r;
	}

	public void setRed(float r) {
		color.r = r;
	}

	public float getGreen() {
		return color.g;
	}

	public void setGreen(float g) {
		color.g = g;
	}

	public float getBlue() {
		return color.b;
	}

	public void setBlue(float b) {
		color.b = b;
	}

	public float getAlpha() {
		return color.a;
	}

	public void setAlpha(float a) {
		color.a = a;
	}
}
