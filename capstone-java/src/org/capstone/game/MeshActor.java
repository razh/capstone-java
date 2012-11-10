package org.capstone.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshActor extends Actor {
	protected ShaderProgram shaderProgram;
	protected Color color;

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
		shaderProgram.setUniformf("translate", this.x, this.y);
		shaderProgram.setUniformf("scale", this.width, this.height);
		shaderProgram.setUniformf("v_color", this.color);
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}
	
	public void act(float delta) {
		super.act(delta);
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
