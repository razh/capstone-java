package org.capstone.game.entities;

import java.util.ArrayList;

import org.capstone.game.CircleMeshActor;
import org.capstone.game.MeshActor;
import org.capstone.game.MeshType;
import org.capstone.game.RectMeshActor;
import org.capstone.game.State;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Entity {
	protected MeshActor meshActor;
	protected MeshType meshType; 
	protected int team = 0;
	protected boolean takingFire = false;
	protected ArrayList<Weapon> weapons;

	public Entity(MeshType type, float x, float y, Color color, float width, float height) {
		setMeshType(type);
		if (type == MeshType.CircleMeshActor)
			meshActor = new CircleMeshActor();
		else if (type == MeshType.RectMeshActor)
			meshActor = new RectMeshActor();
		else
			meshActor = new MeshActor();
		
		meshActor.setEntity(this);
		
		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);

		weapons = new ArrayList<Weapon>();
	}

	public void act(float delta) {
		Actor enemy = this.getNearestActor(State.getStage().getEntities().getChildren());
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
			if (actors[i] instanceof MeshActor && ((MeshActor) actors[i]).getEntity().getTeam() != getTeam()) {
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

			meshActor.addAction(
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
							(((MeshActor) actor).getEntity()).takingFire = false;
							return true;
						}
					}
				)
			);
		}
	}
	
	public float getX() {
		return meshActor.getX();
	}
	
	public void setX(float x) {
		meshActor.setX(x);
	}
	
	public float getY() {
		return meshActor.getY();
	}
	
	public void setY(float y) {
		meshActor.setY(y);
	}
	
	public void setPosition(float x, float y) {
		meshActor.setPosition(x, y);
	}
	
	public float getWidth() {
		return meshActor.getWidth();
	}
	
	public void setWidth(float width) {
		meshActor.setWidth(width);
	}
	
	public float getHeight() {
		return meshActor.getHeight();
	}
	
	public void setHeight(float height) {
		meshActor.setHeight(height);
	}
	
	public Color getColor() {
		return meshActor.getColor();
	}	
	
	public void setColor(Color color) {
		meshActor.setColor(color);
	}
	
	public float getVelocityX() {
		return meshActor.getVelocityX();
	}
	
	public void setVelocityX(float velocityX) {
		meshActor.setVelocityX(velocityX);
	}
	
	public float getVelocityY() {
		return meshActor.getVelocityY();
	}
	
	public void setVelocityY(float velocityY) {
		meshActor.setVelocityY(velocityY);
	}
	
	public Vector2 getVelocity() {
		return meshActor.getVelocity();
	}
	
	public void setVelocity(float velocityX, float velocityY) {
		meshActor.setVelocity(velocityX, velocityY);
	}
	
	public void addAction(Action action) {
		meshActor.addAction(action);
	}

	public MeshType getMeshType() {
		return meshType;
	}

	public void setMeshType(MeshType meshType) {
		this.meshType = meshType;
	}

	public MeshActor getMeshActor() {
		return meshActor;
	}

	public void setMeshActor(MeshActor meshActor) {
		this.meshActor = meshActor;
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

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
}
