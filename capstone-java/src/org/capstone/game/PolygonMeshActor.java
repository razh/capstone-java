package org.capstone.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PolygonMeshActor extends MeshActor {
	private Mesh mesh;
	private float[] boundingVertices;
	private float xmin = Float.MAX_VALUE;
	private float ymin = Float.MAX_VALUE;
	private float xmax = Float.MIN_VALUE;
	private float ymax = Float.MIN_VALUE;

	public PolygonMeshActor() {
		super();
	}

	public Mesh getMesh() {
		return mesh;
	}

	protected boolean hasMesh() {
		return mesh != null;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public int getNumVertices() {
		return boundingVertices.length / 2;
	}

	public void setVertices(float[] vertices) {
		this.boundingVertices = vertices;

		createMesh();
	}

	private void createMesh() {
		int vtxCount = getNumVertices();
		if (vtxCount == 0)
			return;

		int numVertices = (vtxCount + 1) * 2;
		int numIndices  = vtxCount + 2; // Include center and one rotation.

		mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                true, numVertices, numIndices,
		                new VertexAttribute(Usage.Position, 2,
		                                    ShaderProgram.POSITION_ATTRIBUTE));

		float[] vertices = new float[numVertices];
		short[] indices  = new short[numIndices];

		int vtxIndex = 0;
		int idxIndex = 0;

		// Center of polygon.
		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = 0.0f;

		indices[idxIndex++] = 0;

		float x, y;
		for (int i = 0; i < vtxCount; i++) {
			x =  boundingVertices[2 * i];
			y = -boundingVertices[2 * i + 1]; // Screen is flipped vertically.

			if (x < xmin)
				xmin = x;
			if (x > xmax)
				xmax = x;
			if (y < ymin)
				ymin = y;
			if (y > ymax)
				ymax = y;

			vertices[vtxIndex++] = x;
			vertices[vtxIndex++] = y;

			indices[idxIndex++] = (short) (i + 1);
		}

		// Close triangle fan.
		indices[idxIndex++] = 1;

		mesh.setVertices(vertices);
		mesh.setIndices(indices);
	}

	@Override
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		super.draw(shaderProgram, parentAlpha);
		if (hasMesh())
			getMesh().render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	}
}
