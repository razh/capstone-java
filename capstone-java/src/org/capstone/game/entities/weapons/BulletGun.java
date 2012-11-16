package org.capstone.game.entities.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BulletGun extends Gun {
	protected float speed;
	
	protected Color color;
	protected float radius;	

	public BulletGun(Actor actor, float damage, float rate, float range, float speed,
	                 Color color, float radius) {
		super(actor, damage, rate, range);
	}
	
	public void fire() {
		
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
