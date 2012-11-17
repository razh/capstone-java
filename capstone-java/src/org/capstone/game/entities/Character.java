package org.capstone.game.entities;

import java.util.ArrayList;

import org.capstone.game.CircleMeshActor;
import org.capstone.game.RectMeshActor;
import org.capstone.game.State;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Character extends CircleMeshActor {
	protected int team = 0;
	protected boolean takingFire = false;
	protected ArrayList<Weapon> weapons;

	public Character(float x, float y, Color color, float radius) {
		super();

		setPosition(x, y);
		setColor(color);
		setWidth(radius);
		setHeight(radius);

		weapons = new ArrayList<Weapon>();
	}

	public void act(float delta) {
		super.act(delta);
		
		Actor enemy = this.getNearestActor(State.getStage().getCharacters().getChildren());
		if (enemy == null)
			return;
		
		for (int i = 0; i < weapons.size(); i++) {
			weapons.get(i).setActorAsTarget(enemy);
			weapons.get(i).act(delta);
		}
	}

	public Actor getNearestActor(Actor[] actors) {
		Actor actor = null;
		float distance = Float.POSITIVE_INFINITY;
		float min = Float.POSITIVE_INFINITY;

		for (int i = 0; i < actors.length; i++) {
			if (actors[i] instanceof Character && ((Character) actors[i]).getTeam() != getTeam()) {
				distance = distanceToActor(actors[i]);
				if (distance < min) {
					min = distance;
					actor = actors[i];
				}
			}
		}

		return actor;
	}

	public float distanceToActor(Actor actor) {
		return (float) Math.sqrt((getX() - actor.getX()) *
		                         (getX() - actor.getX()) +
		                         (getY() - actor.getY()) *
		                         (getY() - actor.getY()));
	}

	public Actor getNearestActor(SnapshotArray<Actor> actors) {
		Actor[] actorArray = actors.begin();
		Actor actor = getNearestActor(actorArray);
		actors.end();
		
		return actor;
	}

	public void takeFire() {
		if (!takingFire) {
			takingFire = true;

			addAction(
				sequence(
					parallel(
						color(new Color(getColor().r + 0.784f, getColor().g, getColor().b, 1.0f), 0.05f, Interpolation.pow3),
						sizeBy(20, 20, 0.05f, Interpolation.pow3)
					),
					parallel(
						color(new Color(getColor().r, getColor().g, getColor().b, 1.0f), 0.15f, Interpolation.linear),
						sizeBy(-20, -20, 0.15f, Interpolation.linear)
					),
					new Action() {
						public boolean act(float delta) {
							((Character) this.actor).takingFire = false;
							return true;
						}
					}
				)
			);
		}
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
	
	public void addWeapon(Weapon weapon) {
		weapons.add(weapon);
	}
	
	public void removeWeapon(Weapon weapon) {
		weapons.remove(weapon);
	}
}
