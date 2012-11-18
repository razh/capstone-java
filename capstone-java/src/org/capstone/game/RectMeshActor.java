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
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;

		if (contains(x, y))
			return this;

		return null;
	}

	public boolean contains(float x, float y) {
		x -= getX();
		y -= getY();

		float rotation = getRotation() * MathUtils.degreesToRadians;
		if (rotation != 0.0f) {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			// Rotated coordinates.
			float rX =  cos * x + sin * y;
			float rY = -sin * x + cos * y;

			x = rX;
			y = rY;
		}

		if (Math.abs(x) <= getWidth() && Math.abs(y) <= getHeight())
			return true;

		return false;
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
	public Vector2 getIntersection(float x, float y) {
		// Center of rectangle.
		float cX = getX();
		float cY = getY();

		float rotation = getRotation() * MathUtils.degreesToRadians;
		if (rotation != 0.0f) {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			x -= cX;
			y -= cY;

			// Rotated coordinates.
			float rX =  cos * x + sin * y;
			float rY = -sin * x + cos * y;

			x = rX;
			y = rY;

			cX = 0.0f;
			cY = 0.0f;
		}

		float x0 = cX - getWidth();
		float y0 = cY - getHeight();
		float x1 = cX + getWidth();
		float y1 = cY + getHeight();

		Vector2 point = new Vector2(cX, cY);

		// Divide the areas into a 3 by 3 grid for a total of 9 areas (1 center).
		if (x0 <= x && x <= x1) {
			if (y <= y0) {
				point = intersectionOfTwoLines(cX, cY, x, y, x0, y0, x1, y0);
			} else if (y1 <= y) {
				point = intersectionOfTwoLines(cX, cY, x, y, x0, y1, x1, y1);
			}
		} else if (y0 <= y && y <= y1) {
			if (x <= x0) {
				point = intersectionOfTwoLines(cX, cY, x, y, x0, y0, x0, y1);
			} else if (x1 <= x) {
				point = intersectionOfTwoLines(cX, cY, x, y, x1, y0, x1, y1);
			}
		} else {
			float dx = x - cX;
			float dy = y - cY;

			float m = -dy / dx;
			if (Math.abs(m) > getHeight() / getWidth()) {
				if (y <= y0) {
					point = intersectionOfTwoLines(cX, cY, x, y, x0, y0, x1, y0);
				} else if (y1 <= y) {
					point = intersectionOfTwoLines(cX, cY, x, y, x0, y1, x1, y1);
				}
			} else {
				if (x <= x0) {
					point = intersectionOfTwoLines(cX, cY, x, y, x0, y0, x0, y1);
				} else if (x1 <= x) {
					point = intersectionOfTwoLines(cX, cY, x, y, x1, y0, x1, y1);
				}
			}
		}

		if (rotation != 0) {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			float rX = cos * point.x - sin * point.y;
			float rY = sin * point.x + cos * point.y;

			point.x = rX + getX();
			point.y = rY + getY();
		}

		return point;
	}

	private Vector2 intersectionOfTwoLines(float x0, float y0,
	                                       float x1, float y1,
	                                       float x2, float y2,
	                                       float x3, float y3) {
		float x01 = x0 - x1;
		float x23 = x2 - x3;
		float y01 = y0 - y1;
		float y23 = y2 - y3;

		float c = x01 * y23 - y01 * x23;
		if (Math.abs(c) < State.EPSILON) {
			return null;
		} else {
			float a = x0 * y1 - y0 * x1;
			float b = x2 * y3 - y2 * x3;

			float px = (a * x23 - b * x01) / c;
			float py = (a * y23 - b * y01) / c;

			return new Vector2(px, py);
		}
	}
}
