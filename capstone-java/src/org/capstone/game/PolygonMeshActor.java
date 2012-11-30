package org.capstone.game;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

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
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;

		if (contains(x, y))
			return this;

		return null;
	}

	public boolean contains(float x, float y) {
		// Translate coordinates to polygon space.
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

		x /= getWidth();
		y /= getHeight();

		y = -y; // Flip over vertically.

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
		// Translate coordinates to polygon space.
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

		x /= getWidth();
		y /= getHeight();

		y = -y; // Flip over vertically.

		if (Math.abs(x) < State.EPSILON && Math.abs(y) < State.EPSILON)
			return new Vector2(getX(), getY());
		// http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect


    // http://www.softsurfer.com/Archive/algorithm_0111/algorithm_0111.htm
    // Segment parameters.
		// float tEnter = 0.0f;
		// float tLeave = 1.0f;

		// // From (0, 0) to (x, y).
		// float dx = x;
		// float dy = y;

		//  Vector2 point = new Vector2(0.0f, 0.0f);

		// int numVertices = getNumVertices();
		// float xi, yi, xj, yj;
		// float edgeNormalX, edgeNormalY;

		// float t, n, d;

		// n = 0.0f;
		// d = 0.0f;

		// for (int i = 0; i < numVertices; i++) {
		// 	xi = boundingVertices[2 * i];
		// 	yi = boundingVertices[2 * i + 1];
		// 	xj = boundingVertices[(2 * (i + 1)) % numVertices];
		// 	yj = boundingVertices[(2 * (i + 1) + 1) % numVertices];

		// 	// The normal to the vector (x, y) is (y, -x), assuming clockwise order.
		// 	edgeNormalX = yj - yi;
		// 	edgeNormalY = xi - xj;

		// 	n = Geometry.dot(edgeNormalX, edgeNormalY, xi, yi);
		// 	d = Geometry.dot(edgeNormalX, edgeNormalY, dx, dy);

		// 	// The segment is nearly parallel to the edge.
		// 	if (Math.abs(d) < State.EPSILON) {
		// 		// Segment is outside the polygon. Should not happen.
		// 		if (n < 0)
		// 			return null;
		// 		else
		// 			continue;
		// 	}

		// 	t = n / d;
		// 	// The segment enters across this edge.
		// 	if (d < 0) {
		// 		// New max entering parameter.
		// 		if (t > tEnter) {
		// 			tEnter = t;
		// 			// The segment enters AFTER leaving the polygon.
		// 			if (tEnter > tLeave)
		// 				return null;
		// 		}
		// 	}
		// 	// The segment leaves across this edge.
		// 	else {
		// 		// New min leaving parameter.
		// 		if (t < tLeave) {
		// 			tLeave = t;
		// 			// The segment leaves BEFORE entering the polygon.
		// 			if (tLeave < tEnter)
		// 				return null;
		// 		}
		// 	}
		// }

		// // System.out.println(n + ", " + d);
		// System.out.println(tLeave);

		// return new Vector2(getX() + tLeave * dx, getY() + tLeave * dy);

		// int numVertices = getNumVertices() - 1;
		// float xi, yi, xj, yj;
		// for (int i = 0; i < numVertices - 1; i++) {
		// 	xi = boundingVertices[2 * i];
		// 	yi = boundingVertices[2 * i + 1];
		// 	xj = boundingVertices[2 * (i + 1)];
		// 	yj = boundingVertices[2 * (i + 1) + 1];

		// 	point = Geometry.lineLineIntersection(0.0f, 0.0f, x, y, xi, yi, xj, yj);
		// 	if (point != null)
		// 		break;
		// }

		// point.x += getX();
		// point.y += getY();

//		return point;
	}

	@Override
	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		super.draw(shaderProgram, parentAlpha);
		if (hasMesh())
			getMesh().render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	}
}
