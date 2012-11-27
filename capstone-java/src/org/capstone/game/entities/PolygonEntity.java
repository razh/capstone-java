package org.capstone.game.entities;

import org.capstone.game.MeshType;
import org.capstone.game.PolygonMeshActor;

import com.badlogic.gdx.graphics.Color;

public class PolygonEntity extends Entity {
	public PolygonEntity(float[] vertices, float x, float y, Color color, float width, float height) {
		super(MeshType.PolygonMeshActor, x, y, color, width, height);
		
		((PolygonMeshActor) getMeshActor()).setVertices(vertices);
	}
}
