package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class TextMeshActor extends MeshActor {
	protected Mesh mesh;
	protected char character;
	protected short[] indices;

	protected static float[] vertices;

	/*
		From http://www.maximintegrated.com/app-notes/index.mvp/id/3212:
		For a sixteen-segment display:

		     A1     A2
		   ------ ------
		  | \    |    / |
		F | H \ I|  / J | B
		  | G1  \|/  G2 |
		   ------ ------
		  |     /|\     |
		E | M / L|  \ K | C
		  | /    |    \ |
		   ------ ------
		     D1     D2

		Each of these segments is constructed from two vertices:

		   0------1   2------3
		4  5        6        7  8
		|   \       |       /   |
		|     \     |     /     |
		|       \   |   /       |
		9         A B C         D
		   E------F   G------H
		I         J K L         M
		|       /   |   \       |
		|     /     |     \     |
		|   /       |       \   |
		N  O        P        Q  R
		   S------T   U------V

		where A to N represents 10 to 31.

		Note that everything is flipped vertically on screen.
	*/

	private static final short[] segmentIndices = {
		28, 29, // A1
		30, 31, // A2
		22, 27, // B
		 8, 13, // C
		 0,  1, // D1
		 2,  3, // D2
		 4,  9, // E
		18, 23, // F
		14, 15, // G1
		16, 17, // G2
		19, 24, // H
		20, 25, // I
		21, 26, // J
		 7, 12, // K
		 6, 11, // L
		 5, 10  // M
	};

	private void generateVertices() {
		vertices = new float[64];

		float[] xCoordsHoriz    = {0.25f, 2.25f, 2.75f, 4.75f};
		float[] xCoordsFarDiag  = {0.0f, 0.25f, 2.5f, 4.75f, 5.0f};
		float[] xCoordsNearDiag = {0.0f, 2.25f, 2.5f, 2.75f, 5.0f};
		float[] yCoords         = {0.0f, 0.5f, 3.25f, 3.5f, 3.75f, 6.5f, 7.0f};

		float xScale = 1.0f / 5.0f;
		float yScale = 1.0f / 7.0f;

		int index = 0;
		int i;
		// Top horizontal segments.
		for (i = 0; i < xCoordsHoriz.length; i++) {
			vertices[index++] = xCoordsHoriz[i] * xScale;
			vertices[index++] = yCoords[0] * yScale;
		}

		// Far/distal vertices of top vertical and diagonal segments.
		for (i = 0; i < xCoordsFarDiag.length; i++) {
			vertices[index++] = xCoordsFarDiag[i] * xScale;
			vertices[index++] = yCoords[1] * yScale;
		}

		// Near/proximal vertices of top vertical and diagonal segments.
		for (i = 0; i < xCoordsNearDiag.length; i++) {
			vertices[index++] = xCoordsNearDiag[i] * xScale;
			vertices[index++] = yCoords[2] * yScale;
		}

		// Middle horizontal segments.
		for (i = 0; i < xCoordsHoriz.length; i++) {
			vertices[index++] = xCoordsHoriz[i] * xScale;
			vertices[index++] = yCoords[3] * yScale;
		}

		// Near/proximal vertices of bottom vertical and diagonal segments.
		for (i = 0; i < xCoordsNearDiag.length; i++) {
			vertices[index++] = xCoordsNearDiag[i] * xScale;
			vertices[index++] = yCoords[4] * yScale;
		}

		// Far/distal vertices of bottom vertical and diagonal segments.
		for (i = 0; i < xCoordsFarDiag.length; i++) {
			vertices[index++] = xCoordsFarDiag[i] * xScale;
			vertices[index++] = yCoords[5] * yScale;
		}

		// Bottom horizontal segments.
		for (i = 0; i < xCoordsHoriz.length; i++) {
			vertices[index++] = xCoordsHoriz[i] * xScale;
			vertices[index++] = yCoords[6] * yScale;
		}
	}

	// Segment states.
	// We use ints instead of booleans for readability.
	private static final int[] ON  = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] OFF = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

	// We use Segmental as a guide (slight change to 5), with segments in the order noted above:
	// http://www.dafont.com/segmental.font
	private static final int[] NUM_0 = {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1};
	private static final int[] NUM_1 = {1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0};
	private static final int[] NUM_2 = {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] NUM_3 = {1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] NUM_4 = {0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] NUM_5 = {1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0};
	private static final int[] NUM_6 = {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] NUM_7 = {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1};
	private static final int[] NUM_8 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] NUM_9 = {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] A     = {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] B     = {1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0};
	private static final int[] C     = {1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
	private static final int[] D     = {1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0};
	private static final int[] E     = {1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] F     = {1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] G     = {1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] H     = {0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] I     = {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0};
	private static final int[] J     = {0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private static final int[] K     = {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0};
	private static final int[] L     = {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
	private static final int[] M     = {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0};
	private static final int[] N     = {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0};
	private static final int[] O     = {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
	private static final int[] P     = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] Q     = {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0};
	private static final int[] R     = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0};
	private static final int[] S     = {1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0};
	private static final int[] T     = {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0};
	private static final int[] U     = {0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
	private static final int[] V     = {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1};
	private static final int[] W     = {0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0};
	private static final int[] X     = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1};
	private static final int[] Y     = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0};
	private static final int[] Z     = {1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1};

	public TextMeshActor(char character, float x, float y, Color color, float width, float height) {
		super();

		setChar(character);
		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);

		createMesh();
	}

	public char getChar() {
		return character;
	}

	public void setChar(char character) {
		this.character = character;
	}

	private void createMesh() {
		if (vertices == null) {
			generateVertices();
		}

		switch (getChar()) {
			case '0':
				setIndices(generateIndices(NUM_0));
				break;

			case '1':
				setIndices(generateIndices(NUM_1));
				break;

			case '2':
				setIndices(generateIndices(NUM_2));
				break;

			case '3':
				setIndices(generateIndices(NUM_3));
				break;

			case '4':
				setIndices(generateIndices(NUM_4));
				break;

			case '5':
				setIndices(generateIndices(NUM_5));
				break;

			case '6':
				setIndices(generateIndices(NUM_6));
				break;

			case '7':
				setIndices(generateIndices(NUM_7));
				break;

			case '8':
				setIndices(generateIndices(NUM_8));
				break;

			case '9':
				setIndices(generateIndices(NUM_9));
				break;

			case 'A':
				setIndices(generateIndices(A));
				break;

			case 'B':
				setIndices(generateIndices(B));
				break;

			case 'C':
				setIndices(generateIndices(C));
				break;

			case 'D':
				setIndices(generateIndices(D));
				break;

			case 'E':
				setIndices(generateIndices(E));
				break;

			case 'F':
				setIndices(generateIndices(F));
				break;

			case 'G':
				setIndices(generateIndices(G));
				break;

			case 'H':
				setIndices(generateIndices(H));
				break;

			case 'I':
				setIndices(generateIndices(I));
				break;

			case 'J':
				setIndices(generateIndices(J));
				break;

			case 'K':
				setIndices(generateIndices(K));
				break;

			case 'L':
				setIndices(generateIndices(L));
				break;

			case 'M':
				setIndices(generateIndices(M));
				break;

			case 'N':
				setIndices(generateIndices(N));
				break;

			case 'O':
				setIndices(generateIndices(O));
				break;

			case 'P':
				setIndices(generateIndices(P));
				break;

			case 'Q':
				setIndices(generateIndices(Q));
				break;

			case 'R':
				setIndices(generateIndices(R));
				break;

			case 'S':
				setIndices(generateIndices(S));
				break;

			case 'T':
				setIndices(generateIndices(T));
				break;

			case 'U':
				setIndices(generateIndices(U));
				break;

			case 'V':
				setIndices(generateIndices(V));
				break;

			case 'W':
				setIndices(generateIndices(W));
				break;

			case 'X':
				setIndices(generateIndices(X));
				break;

			case 'Y':
				setIndices(generateIndices(Y));
				break;

			case 'Z':
				setIndices(generateIndices(Z));
				break;
		}

		int numVertices = vertices.length;
		int numIndices  = indices.length;

		mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                true, numVertices, numIndices,
		                new VertexAttribute(Usage.Position, 2,
		                                    ShaderProgram.POSITION_ATTRIBUTE));
		mesh.setVertices(vertices);
		mesh.setIndices(indices);
	}

	private short[] generateIndices(int[] segmentStates) {
		// Loop through once to get the number of segments we need to draw.
		int numSegments = 0;
		for (int i = 0; i < segmentStates.length; i++) {
			if (segmentStates[i] == 1)
				numSegments++;
		}

		numSegments *= 2;

		short[] indices = new short[numSegments];
		int index = 0;
		for (int i = 0; i < segmentStates.length; i++) {
			if (segmentStates[i] == 1) {
				indices[index++] = segmentIndices[2 * i];
				indices[index++] = segmentIndices[2 * i + 1];
			}
		}

		return indices;
	}

	private void setIndices(short[] indices) {
		this.indices = indices;
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
			getMesh().render(shaderProgram, GL20.GL_LINES);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;

		if (contains(x, y))
			return this;

		return null;
	}

	// Reusing same code as RectMeshActor. Not cool.
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
}
