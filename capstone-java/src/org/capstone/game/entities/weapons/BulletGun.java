package org.capstone.game.entities.weapons;

import org.capstone.game.MeshActor;
import org.capstone.game.State;
import org.capstone.game.entities.Bullet;
import org.capstone.game.entities.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BulletGun extends Gun {
	protected float speed;

	protected Color color;
	protected float radius;

	public BulletGun(Actor actor, float damage, float rate, float range, float speed,
	                 Color color, float radius) {
		super(actor, damage, rate, range);
		
		setSpeed(speed);
		setColor(color);
		setRadius(radius);
	}
	
	public BulletGun(Entity entity, float damage, float rate, float range, float speed,
                     Color color, float radius) {
		this(entity.getActor(), damage, rate, range, speed, color, radius);
	}

	public void fire() {
		Vector2 point = ((MeshActor) actor).getIntersection(getTargetX(), getTargetY()); 
		if (point == null)
			return;
		
		Bullet bullet = new Bullet(point.x, point.y, color, radius);
		if (actor instanceof MeshActor)
			bullet.setTeam(((MeshActor) actor).getEntity().getTeam());

		bullet.setVelocityX(getTargetX() - actor.getX());
		bullet.setVelocityY(getTargetY() - actor.getY());

		float magnitude = (float) Math.sqrt(bullet.getVelocityX() *
		                                    bullet.getVelocityX() +
		                                    bullet.getVelocityY() *
		                                    bullet.getVelocityY());
		
		bullet.setVelocityX(bullet.getVelocityX() / (magnitude / speed));
		bullet.setVelocityY(bullet.getVelocityY() / (magnitude / speed));
		
		State.getStage().addProjectile(bullet);
		
		setFiring(false);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}