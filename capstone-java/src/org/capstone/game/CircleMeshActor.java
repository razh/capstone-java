package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class CircleMeshActor extends MeshActor {
	protected static Mesh mesh;

	public CircleMeshActor() {
		if (mesh == null) {
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
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;

		float dx = x - getX();
		float dy = y - getY();

		if (Math.sqrt(dx * dx + dy * dy) < getWidth()) {
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
			getMesh().render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	}

	public void act(float delta) {
		super.act(delta);

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		if (getWidth() > getX()) {
			setX(getWidth());
			setVelocityX(-getVelocityX());
		}
		if (getX() + getWidth() > width) {
			setX(width - getWidth());
			setVelocityX(-getVelocityX());
		}
		if (getHeight() > getY()) {
			setY(getHeight());
			setVelocityY(-getVelocityY());
		}
		if (getY() + getHeight() > height) {
			setY(height - getHeight());
			setVelocityY(-getVelocityY());
		}
	}

	@Override
	public Vector2 getIntersection(float x1, float y1) {
		float x0 = getX();
		float y0 = getY();
		float r0 = getWidth();

		float dx = x1 - x0;
		float dy = y1 - y0;

		float length = (float) Math.sqrt(dx * dx + dy * dy);
		dx = dx / length;
		dy = dy / length;

		return new Vector2(x0 + dx * r0, y0 + dy * r0);
	}

	public boolean intersectsRect(RectMeshActor actor) {
		float rotation = getRotation() * MathUtils.degreesToRadians;
		if (actor.getRotation() != 0) {
			float x0 = -actor.getWidth();
			float y0 = -actor.getHeight();
			float x1 = actor.getX() - actor.getWidth();
			float y1 = actor.getX() - actor.getWidth();
			float x2 = actor.getX() - actor.getWidth();
			float y2 = actor.getX() - actor.getWidth();
			float x3 = actor.getX() - actor.getWidth();
			float y3 = actor.getX() - actor.getWidth();
		}

		return false;
	}

	public boolean intersectsRect(float x0, float y0,
	                              float x1, float y1,
	                              float x2, float y2,
	                              float x3, float y3) {
		return false;
	}

	public boolean intersectsLine() {
		return true;
	}
}
