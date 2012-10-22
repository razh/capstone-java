package org.capstone.game.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class RectGraphicsComponent extends GraphicsComponent {
	private static Mesh mesh;
	private float width;
	private float height;

	public RectGraphicsComponent(float x, float y, Color color,
	                             float width, float height) {
		super(x, y, color);

		this.setWidth(width);
		this.setHeight(height);

		if (this.mesh == null)
			this.init();
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}


	public void init(int subdivsX, int subdivsY) {
		int numVertices = (subdivsX + 1) * (subdivsY + 1) * 2;
		int numIndices  = (subdivsX + 1) * (subdivsY + 1) * 2;
		// System.out.println("numIdx:" + numIndices);
		this.mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
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
	public void init() {
		this.init(1, 1);
	}

	@Override
	public void render(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("scale", this.getWidth(), this.getHeight());
		shaderProgram.setUniformf("v_color", this.color);
		this.mesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
	}
}
