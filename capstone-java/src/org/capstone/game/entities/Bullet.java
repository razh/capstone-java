package org.capstone.game.entities;

import org.capstone.game.CircleMeshActor;
import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.MeshType;
import org.capstone.game.PolygonMeshActor;
import org.capstone.game.RectMeshActor;
import org.capstone.game.State;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Bullet extends Entity {
	protected boolean collides = true;
	protected float range;
	protected float originX;
	protected float originY;

	public Bullet(float x, float y, Color color, float radius) {
		this(MeshType.CircleMeshActor, x, y, color, radius, radius, -1.0f);
	}

	public Bullet(float x, float y, Color color, float radius, float range) {
		this(MeshType.CircleMeshActor, x, y, color, radius, radius, range);
	}

	public Bullet(MeshType type, float x, float y, Color color, float width, float height) {
		this(type, x, y, color, width, height, -1.0f);
	}

	public Bullet(MeshType type, float x, float y, Color color, float width, float height, float range) {
		super(type, x, y, color, width, height);

		setRange(range);
		setImmortal(true);

		originX = x;
		originY = y;
	}

	public boolean isColliding() {
		return collides;
	}

	public void setCollides(boolean collides) {
		this.collides = collides;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public void act(float delta) {
		super.act(delta);

		boolean removeBullet = false;
		if (isColliding()) {
			SnapshotArray<Actor> characters = State.getStage().getEntities().getChildren();
			Actor[] actors = characters.begin();

			for (int i = 0; i < characters.size; i++) {
				Actor child = actors[i];

				if (child instanceof CircleMeshActor || child instanceof RectMeshActor || child instanceof PolygonMeshActor) {
					Entity entity = ((MeshActor) child).getEntity();
					if (entity.isAlive() && entity.getTeam() != getTeam()) {
						if (intersects(child)) {
							entity.takeFire();
							removeBullet = true;
							break;
						}
					}
				} else if (child instanceof MeshGroup) {
					EntityGroup entityGroup = (EntityGroup) ((MeshGroup) child).getEntity();
					if (entityGroup.isAlive() && entityGroup.getTeam() != getTeam()) {
						if (intersects(entityGroup.getFirstActor())) {
							entityGroup.takeFire();
							removeBullet = true;
							break;
						}
					}
				}
			}

			characters.end();
		}

		if (collides && range >= 0.0f &&
		    range <= Math.sqrt((getX() - originX) *
		                       (getX() - originX) +
		                       (getY() - originY) *
		                       (getY() - originY))) {
			removeBullet = true;
		}

		if (removeBullet) {
			State.getPlayer().addScore(1);
			setVelocity(0.0f, 0.0f);
			setCollides(false);
			addAction(
				sequence(
					parallel(
						color(new Color(getColor().r, getColor().g, getColor().b, 0.0f), 0.5f, Interpolation.pow2),
						sizeBy(5, 5, 0.5f, Interpolation.pow2)
					),
					removeActor()
				)
			);
		}
	}
}
