package org.capstone.game;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MeshActor extends PhysicsActor {
	protected ShaderProgram shaderProgram;
	protected Entity entity;

	public MeshActor() {
		super();
	}
	
	public void act(float delta) {
		super.act(delta);
		if (entity != null)
			entity.act(delta);
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
		shaderProgram.setUniformf("rotation", getRotation());
		shaderProgram.setUniformf("translate", getX(), getY());
		shaderProgram.setUniformf("scale", getWidth(), getHeight());
		shaderProgram.setUniformf("v_color", getColor());
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;
		
		if (x == getX() && y == getY())
			return this;
		
		return null;
	}

	@Override
	public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
		return parentCoords;
	}

	public Vector2 getIntersection(float x, float y) {
		return new Vector2(getX(), getY());
	}
	
	public boolean intersects(Actor actor) {
		return false;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
