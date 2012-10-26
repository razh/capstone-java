package org.capstone.game;

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
			
		}
	}
	
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		this.shaderProgram = shaderProgram;
		
		draw(parentAlpha);
	}
	
	public void draw(float parentAlpha) {
		
	}

	@Override
	public Actor hit(float x, float y) {
		return this;
	}

}
