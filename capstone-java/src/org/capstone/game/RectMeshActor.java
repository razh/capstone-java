package org.capstone.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RectMeshActor extends MeshActor {
	protected static Mesh mesh;

	public RectMeshActor() {
		if (mesh == null) {
			initMesh();
		}
	}

	private void initMesh() {
		initMesh(1, 1);
	}

	private void initMesh(int subdivsX, int subdivsY) {
		int numVertices = (subdivsX + 1) * (subdivsY + 1) * 2;
		int numIndices  = (subdivsX + 1) * (subdivsY + 1) * 2;
		// System.out.println("numIdx:" + numIndices);
		mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                true, numVertices, numIndices,
		                new VertexAttribute(Usage.Position, 2,
		                                    ShaderProgram.POSITION_ATTRIBUTE));

		// Range of -1.0f to 1.0f.
		float subdivWidth  = 2.0f / subdivsX;
		float subdivHeight = 2.0f / subdivsY;

		float[] vertices = new float[numVertices];
		short[] indices  = new short[numIndices];

		int vtxIndex = 0;
		int idxIndex = 0;
		for (int i = 0; i < subdivsX + 1; i++) {
			for (int j = 0; j < subdivsY + 1; j++) {
				vertices[vtxIndex++] = i * subdivWidth  - 1.0f;
				vertices[vtxIndex++] = j * subdivHeight - 1.0f;
			}

			if (i % 2 == 0) {
				for (int j = 0; j < subdivsY + 1; j++) {
					indices[idxIndex++]  = (short) ((subdivsY + 1) * i + j);
					indices[idxIndex++]  = (short) ((subdivsY + 1) * (i + 1) + j);
				}
			} else {
				for (int j = subdivsY; j >= 0; j--) {
					indices[idxIndex++]  = (short) ((subdivsY + 1) * i + j);
					indices[idxIndex++]  = (short) ((subdivsY + 1) * (i + 1) + j);
				}
			}
		}

		mesh.setVertices(vertices);
		mesh.setIndices(indices);
	}

	@Override
	public Actor hit(float x, float y) {
		if (this.x - width  / 2 < x && x < this.x + width  / 2 &&
		    this.y - height / 2 < y && y < this.y + height / 2) {
			return this;
		}

		return null;
	}

	protected Mesh getMesh() {
		return mesh;
	}

	protected boolean hasMesh() {
		return mesh != null;
	}

	@Override
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		super.draw(shaderProgram, parentAlpha);
		if (hasMesh())
			getMesh().render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
	}
}
