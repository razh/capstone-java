package org.capstone.game;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MeshStage extends Stage {
	private MeshGroup root;
	private ShaderProgram shaderProgram;

	private MeshGroup entities;
	private MeshGroup projectiles;
	private MeshGroup text;
	private MeshGroup tests;
	
	private static boolean gl20 = Gdx.graphics.isGL20Available();
	
	// Allows us to set colors and stuff with actions.
	private Actor colorActor;

	public MeshStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}

	public MeshStage(float width, float height, boolean stretch) {
		super(width, height, stretch);
		if (width != State.getWidth() && height != State.getHeight()) {
			setCamera(new OrthographicCamera());
			setViewport(State.getWidth(), State.getHeight(), stretch);
		}

		root = new MeshGroup();
		root.setStage(this);

		entities = new MeshGroup();
		projectiles = new MeshGroup();
		text = new MeshGroup();
		tests = new MeshGroup();

		addActor(entities);
		addActor(projectiles);
		addActor(text);
		addActor(tests);
		
		colorActor = new Actor();
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
		if (!gl20) {
			drawGL10();
		} else {
			drawGL20();
		}
	}
	
	private void drawGL10() {
		getCamera().update();
		
		
		Gdx.gl10.glLoadMatrixf(getCamera().combined.getValues(), 0);
		
		entities.drawGL10(1.0f);
		projectiles.drawGL10(1.0f);
		text.drawGL10(1.0f);
		
		if (State.debugRendering)
			tests.drawGL10(1.0f);		
	}
	
	private void drawGL20() {
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
		entities.act(delta);
		projectiles.act(delta);
		text.act(delta);
		
		colorActor.act(delta);

		if (State.debugRendering)
			tests.act(delta);
	}

	public MeshGroup getRoot() {
		return root;
	}
	
	public Color getColor() {
		return colorActor.getColor();
	}

	public void setColor(Color color) {
		colorActor.setColor(color);
	}
	
	public void addAction(Action action) {
		colorActor.addAction(action);
	}

	@Override
	public Actor hit(float stageX, float stageY, boolean touchable) {
		Vector2 actorCoords = Vector2.tmp;
		getRoot().parentToLocalCoordinates(actorCoords.set(stageX, stageY));
		return getRoot().hit(actorCoords.x, actorCoords.y, touchable);
	}
	
	public void clearActors() {
		entities.clear();
		projectiles.clear();
		text.clear();
		tests.clear();
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
