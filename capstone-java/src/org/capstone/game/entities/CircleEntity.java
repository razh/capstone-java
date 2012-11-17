package org.capstone.game.entities;

import org.capstone.game.MeshType;

import com.badlogic.gdx.graphics.Color;

public class CircleEntity extends Entity {

	public CircleEntity(float x, float y, Color color, float radius) {
		super(MeshType.CircleMeshActor, x, y, color, radius, radius);
	}
}
