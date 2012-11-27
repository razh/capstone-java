package org.capstone.game;

import com.badlogic.gdx.graphics.Mesh;

public class PolygonMeshActor extends MeshActor {
	private Mesh mesh;
	private float[] vertices;

	public Mesh getMesh() {
		return mesh;
	}
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public int getNumVertices() {
		return vertices.length;
	}
}
