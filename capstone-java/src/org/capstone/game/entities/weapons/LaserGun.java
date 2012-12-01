package org.capstone.game.entities.weapons;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.State;
import org.capstone.game.entities.Entity;
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
	protected float laserWidth;

	public LaserGun(Actor actor, float damage, float rate, float range,
	                Color color, float width) {
		super(actor, damage, rate, range);

		setColor(color);
		laserBeam = new LaserBeam(actor, getTargetX(), getTargetY(), getColor(), width);
		setLaserWidth(width);
	}

	public LaserGun(Entity entity, float damage, float rate, float range,
	                Color color, float width) {
		this(entity.getActor(), damage, rate, range, color, width);
	}
	
	public LaserGun(LaserGun gun) {
		super(gun);
		
		setColor(gun.getColor());
		laserBeam = new LaserBeam(actor, getTargetX(), getTargetY(), getColor(), 0);
		setLaserWidth(gun.getLaserWidth());
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

	public float getLaserWidth() {
		return laserWidth;
	}

	public void setLaserWidth(float laserWidth) {
		this.laserWidth = laserWidth;
		laserBeam.setWidth(laserWidth);
	}

	public void act(float delta) {
		super.act(delta);

		if (isDrawingBeam() && !isFiring()) {
			laserBeam.addAction(removeActor());
			setDrawingBeam(false);
		}
	}

	public void fire() {
		super.fire();
		
		if (actor != laserBeam.getActor())
			laserBeam.setActor((MeshActor) actor);

		if (targetActor == null)
			return;

		Vector2 point = null;
		if (targetActor instanceof MeshActor) {
			point = ((MeshActor) targetActor).getIntersection(actor.getX(), actor.getY());
			((MeshActor) targetActor).getEntity().takeFire();
		} else if (targetActor instanceof MeshGroup) {
			point = ((MeshGroup) targetActor).getIntersection(actor.getX(), actor.getY());
			((MeshGroup) targetActor).getEntity().takeFire();
		}

		if (point == null)
			return;

		laserBeam.setPosition(point.x, point.y);

		if (!isDrawingBeam()) {
			State.getStage().getProjectiles().addActor(laserBeam);
			setDrawingBeam(true);
		}
	}
}
