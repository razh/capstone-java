package org.capstone.game;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MeshStage extends Stage {
	protected MeshGroup root;
	protected ShaderProgram shaderProgram;

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
	}

	@Override
	public void draw() {
		camera.update();

		shaderProgram.begin();
		shaderProgram.setUniformMatrix("projection", camera.combined);
		shaderProgram.end();
	}
}
