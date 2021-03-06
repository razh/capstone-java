package org.capstone.game.entities;

import java.util.ArrayList;

import org.capstone.game.CircleMeshActor;
import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.MeshType;
import org.capstone.game.PolygonMeshActor;
import org.capstone.game.RectMeshActor;
import org.capstone.game.State;
import org.capstone.game.entities.weapons.Weapon;
import org.capstone.game.entities.weapons.WeaponFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Entity {
	// We use Actor instead of MeshActor so that we can use (Mesh)Groups.
	protected Actor actor;
	protected MeshType meshType;
	protected int team = 0;
	protected boolean takingFire = false;
	protected ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	protected int health = -1;

	private boolean oriented = false;

	public Entity(MeshType type, float x, float y, Color color, float width, float height) {
		setMeshType(type);
		if (type == MeshType.CircleMeshActor)
			actor = new CircleMeshActor();
		else if (type == MeshType.RectMeshActor)
			actor = new RectMeshActor();
		else if (type == MeshType.PolygonMeshActor)
			actor = new PolygonMeshActor();
		else if (type == MeshType.Group)
			actor = new MeshGroup();
		else
			actor = new MeshActor();

		((MeshActor) actor).setEntity(this);

		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);
	}

	public Entity(Entity entity) {
		if (entity.actor instanceof CircleMeshActor) {
			actor = new CircleMeshActor();
		} else if (entity.actor instanceof RectMeshActor) {
			actor = new RectMeshActor();
		} else if (entity.actor instanceof PolygonMeshActor) {
			actor = new PolygonMeshActor();
			((PolygonMeshActor) actor).setVertices(((PolygonMeshActor) entity.actor).getVertices());
		} else {
			actor = new MeshActor();
		}

		((MeshActor) actor).setEntity(this);
		setPosition(entity.getX(), entity.getY());

		setVelocity(entity.getVelocityX(), entity.getVelocityY());
		setWidth(entity.getWidth());
		setHeight(entity.getHeight());
		setRotation(entity.getRotation());
		setColor(entity.getColor());

		meshType = entity.meshType;
		team = entity.team;
		takingFire = entity.takingFire;
		health = entity.health;
		oriented = entity.oriented;

		for (int i = 0; i < entity.getWeapons().size(); i++) {
			weapons.add(WeaponFactory.createWeapon(entity.getWeapons().get(i)));
			weapons.get(i).setActor(actor);
		}
	}

	public void act(float delta) {
		if (isOriented()) {
			actor.setRotation((float) Math.atan2(((MeshActor) actor).getVelocityY(),
			                                     ((MeshActor) actor).getVelocityX()) * MathUtils.radiansToDegrees);
		}

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
			if ((actors[i] instanceof MeshActor && ((MeshActor) actors[i]).getEntity().getTeam() != getTeam()) ||
				(actors[i] instanceof MeshGroup && ((MeshGroup) actors[i]).getEntity().getTeam() != getTeam())) {
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
						color(new Color(getColor().r + 0.784f, getColor().g, getColor().b, getColor().a), 0.05f, Interpolation.pow3),
						sizeBy(20, 20, 0.05f, Interpolation.pow3)
					),
					parallel(
						color(new Color(getColor().r, getColor().g, getColor().b, getColor().a), 0.15f, Interpolation.linear),
						sizeBy(-20, -20, 0.15f, Interpolation.linear)
					),
					new Action() {
						public boolean act(float delta) {
							takingFire = false;
							return true;
						}
					}
				)
			);
		}
	}

	public float getX() {
		return actor.getX();
	}

	public void setX(float x) {
		actor.setX(x);
	}

	public float getY() {
		return actor.getY();
	}

	public void setY(float y) {
		actor.setY(y);
	}

	public void setPosition(float x, float y) {
		actor.setPosition(x, y);
	}

	public float getWidth() {
		return actor.getWidth();
	}

	public void setWidth(float width) {
		actor.setWidth(width);
	}

	public float getHeight() {
		return actor.getHeight();
	}

	public void setHeight(float height) {
		actor.setHeight(height);
	}

	public float getRotation() {
		return actor.getRotation();
	}

	public void setRotation(float degrees) {
		actor.setRotation(degrees);
	}

	public void rotateBy(float amountInDegrees) {
		actor.rotate(amountInDegrees);
	}

	public boolean isOriented() {
		return oriented;
	}

	public void setOriented(boolean oriented) {
		this.oriented = oriented;
	}

	public Color getColor() {
		return actor.getColor();
	}

	public void setColor(Color color) {
		actor.setColor(color);
	}

	public float getVelocityX() {
		return ((MeshActor) actor).getVelocityX();
	}

	public void setVelocityX(float velocityX) {
		((MeshActor) actor).setVelocityX(velocityX);
	}

	public float getVelocityY() {
		return ((MeshActor) actor).getVelocityY();
	}

	public void setVelocityY(float velocityY) {
		((MeshActor) actor).setVelocityY(velocityY);
	}

	public Vector2 getVelocity() {
		return ((MeshActor) actor).getVelocity();
	}

	public void setVelocity(float velocityX, float velocityY) {
		((MeshActor) actor).setVelocity(velocityX, velocityY);
	}

	public void addAction(Action action) {
		actor.addAction(action);
	}

	public MeshType getMeshType() {
		return meshType;
	}

	public void setMeshType(MeshType meshType) {
		this.meshType = meshType;
	}

	public Actor getActor() {
		return actor;
	}

	public MeshActor getMeshActor() {
		return (MeshActor) actor;
	}

	public void setMeshActor(MeshActor meshActor) {
		this.actor = meshActor;
	}

	public Vector2 getIntersection(float x, float y) {
		return ((MeshActor) actor).getIntersection(x, y);
	}

	public boolean intersects(Actor actor) {
		return getMeshActor().intersects(actor);
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public void addWeapon(Weapon weapon) {
		weapon.setActor(getActor());
		weapons.add(weapon);
	}

	public void removeWeapon(Weapon weapon) {
		weapons.remove(weapon);
	}

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void addToHealth(int difference) {
		health += difference;
	}
}
