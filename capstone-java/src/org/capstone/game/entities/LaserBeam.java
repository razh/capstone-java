package org.capstone.game.entities;

import org.capstone.game.MeshActor;
import org.capstone.game.RectMeshActor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LaserBeam extends RectMeshActor {
	protected MeshActor actor;

	public LaserBeam(Actor actor, float x, float y, Color color, float width) {
		setActor((MeshActor) actor);
		
		// x, y is the intersection point on the target actor.
		setPosition(x, y);
		setColor(color);
		setWidth(width);
	}

	public MeshActor getActor() {
		return actor;
	}

	public void setActor(MeshActor actor) {
		this.actor = actor;
	}

	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		Vector2 point = actor.getIntersection(getX(), getY());
		
		float dx = getX() - point.x;
		float dy = getY() - point.y;

		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		
		setHeight(distance * 0.5f);
		
		// dx and dy are switched here, as height is the length of the laser beam.
		setRotation((float) (Math.atan2(dx, -dy) * MathUtils.radiansToDegrees));
		
		float originalX = getX();
		float originalY = getY();

		setPosition(getX() - dx * 0.5f, getY() - dy * 0.5f);

		super.draw(shaderProgram, parentAlpha);
		
		setPosition(originalX, originalY);
	}
}
