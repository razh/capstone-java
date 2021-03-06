package org.capstone.game;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class MeshGroup extends Group {
	protected ShaderProgram shaderProgram;

	private MeshStage stage;
	protected Entity entity;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
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
		if (shaderProgram == null) {
			return;
		}

		// Apply transform.
		// if (transform) {
		// }

		drawChildren(parentAlpha);

		// Reset transform.
		// if (transform) {

		// }
	}

	protected void applyTransform(Matrix4 transform) {}

	protected void resetTransform() {}

	protected void drawChildren(float parentAlpha) {
		parentAlpha *= getColor().a;
		SnapshotArray<Actor> children = getChildren();
		Actor[] actors = children.begin();
		//Rectangle cullingArea = this.cullingArea;

		for (int i = 0, n = children.size; i < n; i++) {
			Actor child = actors[i];

			if (!child.isVisible())
				continue;

			// Because MeshGroup does not inherit from MeshActor.
			if (child instanceof MeshActor)
				((MeshActor) child).draw(shaderProgram, parentAlpha);
			if (child instanceof MeshGroup)
				((MeshGroup) child).draw(shaderProgram, parentAlpha);
		}

		children.end();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (entity != null)
			entity.act(delta);
	}

	@Override
	public MeshStage getStage() {
		return stage;
	}

	public void setStage(MeshStage stage) {
		this.stage = stage;
	}

	@Override
	public void addActor(Actor actor) {
		super.addActor(actor);
	}

	public Actor getFirstActor() {
		return getChildren().get(0);
	}

	@Override
	public float getX() {
		// Get x-coordinate of first child.
		// If first child does not exist, default to the MeshGroup's x-coord.
		if (getChildren().size <= 0)
			return super.getX();

		return getFirstActor().getX();
	}

	@Override
	public float getY() {
		if (getChildren().size <= 0)
			return super.getY();

		return getFirstActor().getY();
	}

	public Vector2 getIntersection(float x, float y) {
		return ((MeshActor) getChildren().get(0)).getIntersection(x, y);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		// The same as Group.hit(), except we do not transform coordinates.
		if (touchable && getTouchable() == Touchable.disabled)
			return null;

		Array<Actor> children = getChildren();
		for (int i = children.size - 1; i >= 0; i--) {
			Actor child = children.get(i);
			if (!child.isVisible())
				continue;

			Actor hit = child.hit(x, y, touchable);

			if (hit != null)
				return hit;
		}
		return null;
	}
}
