package org.capstone.game.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class CircleGraphicsComponent extends GraphicsComponent {
	private static Mesh mesh;
	private float radius;

	public CircleGraphicsComponent(float x, float y, Color color, float radius) {
		super(x, y, color);

		this.setRadius(radius);

		if (this.mesh == null)
			this.init();
	}

	public float getRadius() {
		return this.radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void init(int subdivisions) {
		int numVertices = (subdivisions + 1) * 2;
		int numIndices  = subdivisions + 2; // Include center and one rotation.
		this.mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
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
	public void init() {
		this.init(32);
	}

	@Override
	public void render(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("scale", this.getRadius(), this.getRadius());
		shaderProgram.setUniformf("v_color", this.color);
		this.mesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	}
}
