package org.capstone.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PolygonMeshActor extends MeshActor {
	private Mesh mesh;
	private float[] boundingVertices;
	private float xmin;
	private float ymin;
	private float xmax;
	private float ymax;

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
		return boundingVertices.length;
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

		for (int i = 0; i < vtxCount; i++) {
			vertices[vtxIndex++] = boundingVertices[2 * i];
			vertices[vtxIndex++] = boundingVertices[2 * i + 1];

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
