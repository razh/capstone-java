package org.capstone.game.entities;

import org.capstone.game.MeshType;

import com.badlogic.gdx.graphics.Color;

public class RectEntity extends Entity {
	
	public RectEntity(float x, float y, Color color, float width, float height) {
		super(MeshType.RectMeshActor, x, y, color, width, height);
	}
}
