package org.capstone.game;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CircleMeshActor extends MeshActor {
	protected static Mesh mesh;

	public CircleMeshActor() {
		if (mesh != null) {
			initMesh();
		}
	}

	private void initMesh() {
		initMesh(32);
	}

	private void initMesh(int subdivisions) {
		int numVertices = (subdivisions + 1) * 2;
		int numIndices  = subdivisions + 2; // Include center and one rotation.
		mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                true, numVertices, numIndices,
		                new VertexAttribute(Usage.Position, 2,
		                                    ShaderProgram.POSITION_ATTRIBUTE));

		float subdivAngle = (float) (Math.PI * 2 / subdivisions);

		float[] vertices = new float[numVertices];
		short[] indices  = new short[numIndices];

		int vtxIndex = 0;
		int idxIndex = 0;

		vertices[vtxIndex++] = 0.0f;
		vertices[vtxIndex++] = 0.0f;

		// Center.
		indices[idxIndex++] = 0;

		for (int i = 0; i < subdivisions; i++) {
			vertices[vtxIndex++] = (float) Math.sin(i * subdivAngle);
			vertices[vtxIndex++] = (float) Math.cos(i * subdivAngle);

			indices[idxIndex++] = (short) (i + 1);
		}

		// Close triangle fan.
		indices[idxIndex++] = 1;

		mesh.setVertices(vertices);
		mesh.setIndices(indices);
	}

	@Override
	public Actor hit(float x, float y) {
		float dx = x - this.x;
		float dy = y - this.y;

		if (Math.sqrt(dx * dx + dy * dy) < width) {
			return this;
		}

		return null;
	}
}
