package org.capstone.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshActor extends Actor {
	protected Mesh mesh;
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
		shaderProgram.setUniformf("scale", this.width, this.height);
		shaderProgram.setUniformf("v_color", this.color);
		mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	}

	@Override
	public Actor hit(float x, float y) {
		return null;
	}

}
