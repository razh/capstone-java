package org.capstone.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshActor extends Actor {
	protected ShaderProgram shaderProgram;

	public MeshActor() {
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
		shaderProgram.setUniformf("translate", getX(), getY());
		shaderProgram.setUniformf("scale", getWidth(), getHeight());
		shaderProgram.setUniformf("v_color", getColor());
	}

	public Actor hit(float x, float y) {
		return null;
	}
}
