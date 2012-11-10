package org.capstone.game;

import java.awt.event.InputEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;

public class MeshStage extends Stage {
	protected MeshGroup root;
	protected ShaderProgram shaderProgram;
	protected MeshActor[] pointerOverActors = new MeshActor[20];
	
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
	
	@Override
	public void addActor(Actor actor) {
		root.addActor(actor);
	}
	
	public void act(float delta) {
		root.act(delta);
	}
}
