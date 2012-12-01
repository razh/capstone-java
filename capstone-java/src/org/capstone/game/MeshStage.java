package org.capstone.game;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MeshStage extends Stage {
	private MeshGroup root;
	private ShaderProgram shaderProgram;

	private MeshGroup entities;
	private MeshGroup projectiles;
	private MeshGroup text;
	private MeshGroup tests;

	public MeshStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);

		root = new MeshGroup();
		root.setStage(this);
		
		entities = new MeshGroup();
		projectiles = new MeshGroup();
		text = new MeshGroup();
		tests = new MeshGroup();
		
		root.addActor(entities);
		root.addActor(projectiles);
		root.addActor(text);
		root.addActor(tests);
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

		getCamera().update();

		shaderProgram.begin();
		shaderProgram.setUniformMatrix("projection", getCamera().combined);
		entities.draw(shaderProgram, 1.0f);
		projectiles.draw(shaderProgram, 1.0f);
		text.draw(shaderProgram, 1.0f);

		if (State.debugRendering)
			tests.draw(shaderProgram, 1.0f);

		shaderProgram.end();
	}

	@Override
	public void addActor(Actor actor) {
		root.addActor(actor);
	}

	public void act(float delta) {
		root.act(delta);
	}

	public MeshGroup getRoot() {
		return root;
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		Vector2 actorCoords = Vector2.tmp;
		getRoot().parentToLocalCoordinates(actorCoords.set(stageX, stageY));
		return getRoot().hit(actorCoords.x, actorCoords.y, touchable);
	}

	//----------------------------------------------------------------------------
	//  ENTITIES
	//----------------------------------------------------------------------------
	public void addEntity(Actor actor) {
		entities.addActor(actor);
	}

	public void addEntity(Entity entity) {
		addEntity(entity.getActor());
	}

	public MeshGroup getEntities() {
		return entities;
	}

	//----------------------------------------------------------------------------
	//  PROJECTILES
	//----------------------------------------------------------------------------
	public void addProjectile(Actor actor) {
		projectiles.addActor(actor);
	}

	public void addProjectile(Entity projectile) {
		addProjectile(projectile.getActor());
	}

	public MeshGroup getProjectiles() {
		return projectiles;
	}

	//----------------------------------------------------------------------------
	//  TEXT
	//----------------------------------------------------------------------------
	public void addText(Actor actor) {
		text.addActor(actor);
	}

	public void addText(Entity text) {
		addText(text.getActor());
	}

	public MeshGroup getText() {
		return text;
	}

	//----------------------------------------------------------------------------
	//  TESTING
	//----------------------------------------------------------------------------
	public void addTest(Actor actor) {
		tests.addActor(actor);
	}

	public void addTest(Entity test) {
		addTest(test.getActor());
	}

	public MeshGroup getTests() {
		return tests;
	}
}
