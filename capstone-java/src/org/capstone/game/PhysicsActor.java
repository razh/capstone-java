package org.capstone.game;

public class PhysicsActor extends MeshActor {
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
	
	public void setVelocity(float velocityX, float velocityY) {
		setVelocityX(velocityX);
		setVelocityY(velocityY);
	}
}
