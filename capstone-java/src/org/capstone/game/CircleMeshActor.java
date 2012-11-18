package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	public Vector2 getIntersection(float x, float y) {
		float x0 = getX();
		float y0 = getY();
		float r0 = getWidth();

		float dx = x - x0;
		float dy = y - y0;

		float length = (float) Math.sqrt(dx * dx + dy * dy);
		dx = dx / length;
		dy = dy / length;

		return new Vector2(x0 + dx * r0, y0 + dy * r0);
	}

	public boolean intersects(Actor actor) {
		if (actor instanceof CircleMeshActor) {
			return intersects((CircleMeshActor) actor);
		} else if (actor instanceof RectMeshActor) {
			return intersects((RectMeshActor) actor);
		} else {
			return false;
		}
	}

	public boolean intersects(CircleMeshActor actor) {
		float dx = actor.getX() - getX();
		float dy = actor.getY() - getY();

		float distance = (float) Math.sqrt(dx * dx + dy * dy);

		if (distance < getWidth() + actor.getWidth())
			return true;

		return false;
	}

	public boolean intersects(RectMeshActor actor) {
		/*
			(x0, y0) o----------o (x1, y1)
			         |          |
			         |          |
			         |          |
			(x3, y3) o----------o (x2, y2)
		*/

		float width  = actor.getWidth();
		float height = actor.getHeight();

		float x0 = -width;
		float y0 = -height;
		float x1 =  width;
		float y1 = -height;
		float x2 =  width;
		float y2 =  height;
		float x3 = -width;
		float y3 =  height;

		// System.out.println(x0 + ", " + y0 + ", " + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ", " + x3 + ", " + y3);

		float rotation = actor.getRotation() * MathUtils.degreesToRadians;
		if (rotation != 0.0f) {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			float rX0 = cos * x0 - sin * y0;
			float rY0 = sin * x0 + cos * y0;
			float rX1 = cos * x1 - sin * y1;
			float rY1 = sin * x1 + cos * y1;
			float rX2 = cos * x2 - sin * y2;
			float rY2 = sin * x2 + cos * y2;
			float rX3 = cos * x3 - sin * y3;
			float rY3 = sin * x3 + cos * y3;

			x0 = rX0;
			y0 = rY0;
			x1 = rX1;
			y1 = rY1;
			x2 = rX2;
			y2 = rY2;
			x3 = rX3;
			y3 = rY3;

			// System.out.println(x0 + ", " + y0 + ", " + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ", " + x3 + ", " + y3);
		}

		float cX = actor.getX();
		float cY = actor.getY();

		x0 += cX;
		y0 += cY;
		x1 += cX;
		y1 += cY;
		x2 += cX;
		y2 += cY;
		x3 += cX;
		y3 += cY;

		// System.out.println(x0 + ", " + y0 + ", " + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ", " + x3 + ", " + y3);


		return // actor.contains(getX(), getY()) ||
		       intersectsLine(x0, y0, x1, y1);// ||
		       // intersectsLine(x1, y1, x2, y2) ||
		       // intersectsLine(x2, y2, x3, y3) ||
		       // intersectsLine(x3, y3, x1, y1);
	}

	// Line intersection test is not used much as actor.contains() suffices.
	public boolean intersectsLine(float x0, float y0, float x1, float y1) {
		/* Given the parametric formula of a line segment formed by the points
		   (x, y) and (i, j):

				x(t) = tx + (1 - t)i
				y(t) = ty + (1 - t)j

			Plug these into the equation of a circle: x(t)^2 + y(t)^2 = r^2 (first,
			translate coordinates to the center) to get:

				[tx + (1 - t)i]^2 + [ty + (1 - t)j]^2 - r^2 = 0

		  which expands to:

				[x^2 + i^2 - 2ix + (y - j)^2]t^2 + [2(ix + yj - i^2 - j^2)]t + i^2 + j^2 - r^2

		  such that the coefficients of the quadratic equation are:

				a = x^2 + i^2 - 2ix + (y - j)^2
				b = 2(xi + yj - i^2 - j^2)
				c = i^2 + j^2 - r^2

			The discriminant is thus: b^2 - 4ac.

			If the discriminant = 0, there is one intersection point.
			If the discriminant > 0, there are two intersection points.
			If the discriminant < 0, there are no intersection points.
		*/

		// Transform coordinates to circle space.
		x0 -= getX();
		y0 -= getY();
		x1 -= getX();
		y1 -= getY();
		float r = getWidth();

		float a = x0 * x0 + x1 * x1 - 2.0f * x1 * x0 + (y0 - y1) * (y0 - y1);
		float b = 2.0f * (x0 * x1 + y0 * y1 - x1 * x1 - y1 * y1);
		float c = x1 * x1 + y1 * y1 - r * r;

		float d = b * b - 4.0f * a * c;

		if (Gdx.input.isKeyPressed(Input.Keys.F)) {
			System.out.println("x0: " + x0 + ", y0: " + y0 + ", x1: " + x1 + ", y1: " + y1);
			System.out.println("a: " + a + ", b: " + b + ", c: " + c);
			System.out.println(d);
		}

		if (d < 0.0f)
			return false;

		if (d == 0.0f) {
			float t = (float) (-b / (2.0f * a));
			System.out.println(t);
			return 0.0f <= t && t <= 1.0f;
		}

		float t0 = (float) ((-b - Math.sqrt(d)) / (2.0f * a));
		float t1 = (float) ((-b + Math.sqrt(d)) / (2.0f * a));

		System.out.println(t0 + ", " + t1);

		if (0.0f <= t0 && t0 <= 1.0f || 0.0f <= t1 && t1 <= 1.0f )
			return true;
		else
			return false;

		// System.out.println(t0 + ", " + t1);

		// return b * b - 4.0f * a * c >= 0.0f;
		// return d >= 0.0f;
	}

	public boolean intersectsLine(float x0, float y0, float x1, float y1, float rotation) {
		if (rotation == 0.0f) {
			return intersectsLine(x0, y0, x1, y1);
		} else {
			float cos = (float) Math.cos(rotation);
			float sin = (float) Math.sin(rotation);

			float rX0 =  cos * x0 + sin * y0;
			float rY0 = -sin * x0 + cos * y0;
			float rX1 =  cos * x1 + sin * y1;
			float rY1 = -sin * x1 + cos * y1;

			return intersectsLine(rX0, rY0, rX1, rY1);
		}
	}
}
