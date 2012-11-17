package org.capstone.game.entities.weapons;

import org.capstone.game.MeshActor;
import org.capstone.game.State;
import org.capstone.game.entities.LaserBeam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class LaserGun extends Gun {
	protected Color color;
	private Actor targetActor;
	private boolean drawingBeam = false;
	protected LaserBeam laserBeam;

	public LaserGun(Actor actor, float damage, float rate, float range,
	                Color color, float width) {
		super(actor, damage, rate, range);
		
		setColor(color);
		laserBeam = new LaserBeam(actor, getTargetX(), getTargetY(), getColor(), width);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Actor getTargetActor() {
		return targetActor;
	}

	@Override
	public void setActorAsTarget(Actor targetActor) {
		super.setActorAsTarget(targetActor);
		
		this.targetActor = targetActor;
	}

	public boolean isDrawingBeam() {
		return drawingBeam;
	}

	public void setDrawingBeam(boolean drawingBeam) {
		this.drawingBeam = drawingBeam;
	}
	
	public void act(float delta) {
		super.act(delta);
		
		if (isDrawingBeam() && !isFiring()) {
			laserBeam.addAction(removeActor());
			setDrawingBeam(false);
		}
	}
	
	public void fire() {
	}
}
