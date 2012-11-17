package org.capstone.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PhysicsActor extends Actor {
	private float velocityX;
	private float velocityY;
	
	public PhysicsActor() {
		super();
	}
	
	public void act(float delta) {
		super.act(delta);
		
		setX(getX() + getVelocityX() * delta);
		setY(getY() + getVelocityY() * delta);
	}

	public float getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}
	
	public Vector2 getVelocity() {
		return new Vector2(velocityX, velocityY);
	}
	
	public void setVelocity(float velocityX, float velocityY) {
		setVelocityX(velocityX);
		setVelocityY(velocityY);
	}
}
