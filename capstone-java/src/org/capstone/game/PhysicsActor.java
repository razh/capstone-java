package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PhysicsActor extends Actor {
	private float velocityX;
	private float velocityY;
	private boolean bounded;
	
	public PhysicsActor() {
		super();

		setBounded(true);
	}
	
	public void act(float delta) {
		super.act(delta);

//		float damping = 0.97f;
//		setVelocityX(getVelocityX() * damping);
//		setVelocityY((getVelocityY() - 800.0f * delta) * damping);

		if (Math.abs(getVelocityX()) < State.EPSILON)
			setVelocityX(0.0f);
		if (Math.abs(getVelocityY()) < State.EPSILON)
			setVelocityY(0.0f);
		setX(getX() + getVelocityX() * delta);
		setY(getY() + getVelocityY() * delta);

		if (isBounded()) {
			float width = Gdx.graphics.getWidth();
			float height = Gdx.graphics.getHeight();
	
			if (getWidth() > getX()) {
				setX(getWidth());
				setVelocityX(-getVelocityX());
			}
			if (getX() + getWidth() > width) {
				setX(width - getWidth());
				setVelocityX(-getVelocityX());
			}
			if (getHeight() > getY()) {
				setY(getHeight());
				setVelocityY(-getVelocityY());
			}
			if (getY() + getHeight() > height) {
				setY(height - getHeight());
				setVelocityY(-getVelocityY());
			}
		}
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

	public boolean isBounded() {
		return bounded;
	}

	public void setBounded(boolean bounded) {
		this.bounded = bounded;
	}
}
