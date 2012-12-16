package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.google.gson.annotations.SerializedName;

public class PolygonMeshActor extends MeshActor {
	private Mesh mesh;

	@SerializedName("vertices")
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

	public float[] getVertices() {
		return boundingVertices;
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
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;

		if (contains(x, y))
			return this;

		return null;
	}

	public boolean contains(float x, float y) {
		Vector2 v = screenToLocalCoordinates(new Vector2(x, y));
		x = v.x;
		y = v.y;

		// System.out.println(x + ", " + y + ", x: " + xmin + ", " + xmax + ", y: " + ymin + ", " + ymax);

		if (xmin > x || x > xmax ||
		    ymin > y || y > ymax) {
			return false;
		}

		int numVertices = getNumVertices();
		boolean contains = false;
		float xi, yi, xj, yj;
		for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
			xi = boundingVertices[2 * i];
			yi = boundingVertices[2 * i + 1];
			xj = boundingVertices[2 * j];
			yj = boundingVertices[2 * j + 1];

			if (((yi > y) != (yj > y)) &&
			    (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) {
				contains = !contains;
			}
		}

		return contains;
	}

	@Override
	public Vector2 getIntersection(float x, float y) {
		Vector2 v = screenToLocalCoordinates(new Vector2(x, y));
		x = v.x;
		y = v.y;

		if (Math.abs(x) < State.EPSILON && Math.abs(y) < State.EPSILON)
			return new Vector2(getX(), getY());

		/*
		             (x, y)
		               ^
		               |
		  (xi, yi) ----o----> (xj, yj)
		               |
		             (0, 0)

		  Line segment (p, p + r) is from (0, 0) to (x, y).
		  Line segment (q, q + s) is from (xi, yi) to (xj, yj).
		*/
		// http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
		int numVertices = getNumVertices();
		float xi, yi, xj, yj;
		// Edge.
		float ex, ey;
		// Parameter along line segment (0, 0) to (x, y).
		float t = 0.0f;
		// Parameter along line segment (xi, yi) to (xj, yj).
		float u = 0.0f;
		// Magnitude of cross product of (x, y) and (xj - xi, yj - yi).
		float cross;

		Vector2 point = null;
		for (int i = 0; i < numVertices; i++) {
			xi = boundingVertices[2 * i];
			yi = boundingVertices[2 * i + 1];
			xj = boundingVertices[(2 * (i + 1)) % boundingVertices.length];
			yj = boundingVertices[(2 * (i + 1) + 1) % boundingVertices.length];

			// s
			ex = xj - xi;
			ey = yj - yi;

			// (r x s).
			cross = Geometry.cross(x, y, ex, ey);
			// Lines are parallel if (r x s) == 0.
			if (Math.abs(cross) < State.EPSILON)
				continue;

			// Compute reciprocal.
			cross = 1.0f / cross;

			// (q - p) x s / (r x s).
			t = Geometry.cross(xi, yi, ex, ey) * cross;
			// (q - p) x r / (r x s)
			u = Geometry.cross(xi, yi, x, y) * cross;
			if (0.0f <= t && t <= 1.0f && 0.0f <= u && u <= 1.0f) {
				point = new Vector2(t * x, t * y);
				break;
			}
		}

		if (point == null)
			return new Vector2(getX(), getY());

		return localToScreenCoordinates(point);
	}

	@Override
	public boolean intersects(Actor actor) {
		if (actor instanceof CircleMeshActor) {
			return intersects((CircleMeshActor) actor);
		} else if (actor instanceof RectMeshActor) {
			return intersects((RectMeshActor) actor);
		} else if (actor instanceof PolygonMeshActor) {
			return intersects((PolygonMeshActor) actor);
		} else {
			return false;
		}
	}

	public boolean intersects(CircleMeshActor actor) {
		return actor.intersects(this);
	}

	@Override
	public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
		// Translate coordinates to polygon space.
		screenCoords.x -= getX();
		screenCoords.y -= getY();

		float rotation = getRotation() * MathUtils.degreesToRadians;
		if (rotation != 0.0f) {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			// Rotated coordinates.
			float rX =  cos * screenCoords.x + sin * screenCoords.y;
			float rY = -sin * screenCoords.x + cos * screenCoords.y;

			screenCoords.x = rX;
			screenCoords.y = rY;
		}

		screenCoords.x /= getWidth();
		screenCoords.y /= getHeight();

		screenCoords.y = -screenCoords.y; // Flip over vertically.

		return screenCoords;
	}

	public Vector2 localToScreenCoordinates(Vector2 localCoords) {
		localCoords.y = -localCoords.y;

		localCoords.x *= getWidth();
		localCoords.y *= getHeight();

		// Rotate back.
		float rotation = getRotation() * MathUtils.degreesToRadians;
		if (rotation != 0.0f) {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			float rX = cos * localCoords.x - sin * localCoords.y;
			float rY = sin * localCoords.x + cos * localCoords.y;

			localCoords.x = rX;
			localCoords.y = rY;
		}

		localCoords.x += getX();
		localCoords.y += getY();

		return localCoords;
	}
	
	@Override
	public void drawGL10(float parentAlpha) {
		super.drawGL10(parentAlpha);
		if (hasMesh())
			getMesh().render(GL10.GL_TRIANGLE_FAN);
		Gdx.gl10.glPopMatrix();
	}

	@Override
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		super.draw(shaderProgram, parentAlpha);
		if (hasMesh())
			getMesh().render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	}
}
