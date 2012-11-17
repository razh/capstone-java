package org.capstone.game.entities.weapons;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Weapon {
	protected Actor actor;
	protected float damage;
	protected float rate;
	protected float range;
	
	protected boolean firing = true;
	protected float time = 0;
	
	protected float targetX = Float.NaN;
	protected float targetY = Float.NaN;
	
	public Weapon(Actor actor, float damage, float rate, float range) {
		setActor(actor);
		setDamage(damage);
		setRate(rate);
		setRange(range);		
	}
	
	public Weapon(Entity entity, float damage, float rate, float range) {
		setActor(entity.getMeshActor());
		setDamage(damage);
		setRate(rate);
		setRange(range);		
	}
	
	public void act(float delta) {
		time += delta;
		if (time > rate) {
			setFiring(true);
			time = 0;
		}
	}
	
	public void setActorAsTarget(Actor actor) {
		if (actor != null) {
			setTarget(actor.getX(), actor.getY());
		} else {
			setTarget(Float.NaN, Float.NaN);
		}
	}
	
	public boolean hasTarget() {
		return targetX != Float.NaN && targetY != Float.NaN;
	}
	
	public boolean targetInRange() {
		if (actor == null)
			return false;
		
		if (range < 0)
			return true;
		
		return range >= Math.sqrt((targetX - actor.getX()) *
		                          (targetX - actor.getX()) +
		                          (targetY - actor.getY()) *
		                          (targetY - actor.getY()));
	}
	
	public void fire() {}
	
	// Getters and setters.
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public boolean isFiring() {
		return firing;
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}
	
	public float getTargetX() {
		return targetX;
	}

	public void setTargetX(float targetX) {
		this.targetX = targetX;
	}

	public float getTargetY() {
		return targetY;
	}

	public void setTargetY(float targetY) {
		this.targetY = targetY;
	}
	
	public void setTarget(float targetX, float targetY) {
		setTargetX(targetX);
		setTargetY(targetY);
	}
}
