package org.capstone.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshActor extends Actor {
	protected ShaderProgram shaderProgram;

	public MeshActor() {
		super();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (shaderProgram != null) {
			draw(parentAlpha);
		}
	}

	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		this.shaderProgram = shaderProgram;

		draw(parentAlpha);
	}

	public void draw(float parentAlpha) {
		shaderProgram.setUniformf("rotation", getRotation());
		shaderProgram.setUniformf("translate", getX(), getY());
		shaderProgram.setUniformf("scale", getWidth(), getHeight());
		shaderProgram.setUniformf("v_color", getColor());
	}

	public Actor hit(float x, float y) {
		return null;
	}

	@Override
	public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
		return parentCoords;
	}

	public Vector2 getIntersection(float x, float y) {
		return new Vector2(getX(), getY());
	}
}
