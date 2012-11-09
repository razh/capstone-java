package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MeshStage extends Stage {
	protected MeshGroup root;
	protected ShaderProgram shaderProgram;
	
	public MeshStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
		
		root = new MeshGroup();
		root.setStage(this);
	}
	
	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}
	
	public void draw(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		
		draw();
	}
	
	@Override
	public void draw() {
		if (shaderProgram == null) {
			return;
		}
		
		camera.update();

		shaderProgram.begin();
		shaderProgram.setUniformMatrix("projection", camera.combined);
		root.draw(shaderProgram, 1.0f);
		shaderProgram.end();
	}
}
