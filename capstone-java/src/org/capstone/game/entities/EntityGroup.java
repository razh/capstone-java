package org.capstone.game.entities;

import org.capstone.game.MeshGroup;
import org.capstone.game.MeshType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

public class EntityGroup extends Entity {
	protected int numSegments;
	protected float segmentDistance;
	protected Array<Entity> segments = new Array<Entity>();
	protected MeshGroup segmentGroup = new MeshGroup();
	
	private boolean oriented = false;

	public EntityGroup(MeshType type, float x, float y, Color color,
	                   float width, float height, int numSegments, float segmentDistance) {
		super(type, x, y, color, width, height);
		
		setNumSegments(numSegments);
		setSegmentDistance(segmentDistance);

		segments.add(this);
		segmentGroup.setEntity(this);
		segmentGroup.addActor(actor);
		
		Entity segment;
		for (int i = 1; i < numSegments; i++) {
//			segment = new Entity(type, x + i * 60.0f, y, new Color(color.r - i * 0.1f, color.g, color.b, color.a), width, height);
			segment = new Entity(type, x, y, color, width, height);
			segmentGroup.addActor(segment.getActor());
			segments.add(segment);
			
			segment.getActor().setTouchable(Touchable.disabled);
		}
	}
	
	public void act(float delta) {
		super.act(delta);

		if (isOriented()) {
			segments.get(0).setRotation((float) Math.atan2(segments.get(0).getVelocityY(), segments.get(0).getVelocityX()) * MathUtils.radiansToDegrees);
		}

		float x0, y0, x1, y1;
		float dx, dy;
		float distance;
		for (int i = 0; i < numSegments - 1; i++) {
			x0 = segments.get(i).getX();
			y0 = segments.get(i).getY();
			x1 = segments.get(i + 1).getX();
			y1 = segments.get(i + 1).getY();
			
			dx = x1 - x0;
			dy = y1 - y0;
			
			distance = (float) Math.sqrt(dx * dx + dy * dy);
			if (distance >= segmentDistance) {
				dx *= segmentDistance / distance;
				dy *= segmentDistance / distance;
				
				segments.get(i + 1).setX(x0 + dx);
				segments.get(i + 1).setY(y0 + dy);
			}
			
			if (isOriented()) {
				segments.get(i + 1).setRotation((float) Math.atan2(dy, dx) * MathUtils.radiansToDegrees);
			}
		}
	}

	public int getNumSegments() {
		return numSegments;
	}

	public void setNumSegments(int numSegments) {
		this.numSegments = numSegments;
	}

	public float getSegmentDistance() {
		return segmentDistance;
	}

	public void setSegmentDistance(float segmentDistance) {
		this.segmentDistance = segmentDistance;
	}
	
	public boolean isOriented() {
		return oriented;
	}

	public void setOriented(boolean oriented) {
		this.oriented = oriented;
	}

	public void takeFire() {
		super.takeFire();
	}
	
	@Override
	public Actor getActor() {
		return segmentGroup;
	}
	
	public Actor getFirstActor() {
		return actor;
	}

}
