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
		// short[] indices = {0,1,5};
		// short[] indices = {0,5,1,6,2,7,3,8,4,9,9,14,8,13,7,12,6,11,5,10};
		// short[] indices = {0,3,1,4,2,5,5,8,4,7,3,6,6,9,7,10,8,11,11,14,10,13,9,12};
		int vtxIndex = 0;
		int idxIndex = 0;
		int v = 0;
		for (int i = 0; i < subdivsX + 1; i++) {
			for (int j = 0; j < subdivsY + 1; j++) {
				vertices[vtxIndex++] = i * subdivWidth  - 1.0f;
				vertices[vtxIndex++] = j * subdivHeight - 1.0f;
				// vertices[vtxIndex++] = 1.0f;
				v++;
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

		// int i = 0;
		// int vcount = 0;
		// while (i < numVertices) {
		// 	System.out.println( vcount++ + ": (" + vertices[i++] + ", " + vertices[i++] + ")" );
		// }
		// i = 0;
		// while (i < numIndices) {
		// 	System.out.print( indices[i++] + ", " );
		// }
		// System.out.println();
		// System.out.println("v:" + v +" n:" + numVertices);

		mesh.setVertices(vertices);
		mesh.setIndices(indices);
	}

	@Override
	public void init() {
		this.init(1, 1);
	}

	@Override
	public void render(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("x_scale", this.getWidth());
		shaderProgram.setUniformf("y_scale", this.getHeight());
		shaderProgram.setUniformf("v_color", this.color);
		this.mesh.render(shaderProgram, GL20.GL_TRIANGLE_STRIP);
	}
}
