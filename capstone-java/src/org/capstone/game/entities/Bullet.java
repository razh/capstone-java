package org.capstone.game.entities;

import org.capstone.game.CircleMeshActor;
import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.RectMeshActor;
import org.capstone.game.State;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Bullet extends CircleEntity {
	protected boolean collides = true;

	public Bullet(float x, float y, Color color, float radius) {
		super(x, y, color, radius);
	}

	public boolean isColliding() {
		return collides;
	}

	public void setCollides(boolean collides) {
		this.collides = collides;
	}

	public void act(float delta) {
		super.act(delta);

		boolean removeBullet = false;
		if (isColliding()) {
			float distance;
			SnapshotArray<Actor> characters = State.getStage().getEntities().getChildren();
			Actor[] actors = characters.begin();

			for (int i = 0; i < characters.size; i++) {
				Actor child = actors[i];

				if (child instanceof CircleMeshActor) {
					if (((MeshActor) child).getEntity().getTeam() != getTeam()) {
						distance = distanceToActor(child);
						if (distance < getWidth() + child.getWidth()) {
							((MeshActor) child).getEntity().takeFire();
							removeBullet = true;
							break;
						}
					}
				} else if (child instanceof RectMeshActor) {
					
				}
			}

			characters.end();
		}

		if (removeBullet) {
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
