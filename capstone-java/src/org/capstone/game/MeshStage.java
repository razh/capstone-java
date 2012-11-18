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

		getCamera().update();

		shaderProgram.begin();
		shaderProgram.setUniformMatrix("projection", getCamera().combined);
		root.draw(shaderProgram, 1.0f);
		shaderProgram.end();
	}

	@Override
	public void addActor(Actor actor) {
		root.addActor(actor);
	}

	private void initEntities() {
		entities = new MeshGroup();
		root.addActor(entities);
	}

	public void addEntity(Actor actor) {
		if (entities == null)
			initEntities();

		entities.addActor(actor);
	}

	public void addEntity(Entity entity) {
		addEntity(entity.getActor());
	}

	public MeshGroup getEntities() {
		if (entities == null)
			initEntities();

		return entities;
	}

	private void initProjectiles() {
		projectiles = new MeshGroup();
		root.addActor(projectiles);
	}

	public void addProjectile(Actor actor) {
		if (projectiles == null)
			initProjectiles();

		projectiles.addActor(actor);
	}

	public void addProjectile(Entity projectile) {
		addProjectile(projectile.getActor());
	}

	public MeshGroup getProjectiles() {
		if (projectiles == null)
			initProjectiles();

		return projectiles;
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
}
