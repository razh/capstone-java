package org.capstone.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TextMeshActor extends MeshActor {
	protected Mesh mesh;
	protected Character character;
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

	A = 10
	B = 11
	C = 12
	D = 13
	E = 14
	F = 15
	G = 16
	H = 17
	I = 18
	J = 19
	K = 20
	L = 21
	M = 22
	N = 23
	O = 24
	P = 25
	Q = 26
	R = 27
	S = 28
	T = 29
	U = 30
	V = 31


		where A to N represents 10 to 31.
	*/

	private static final short[] segmentIndices = {
		 0,  1, // A1
		 2,  3, // A2
		 8, 13, // B
		22, 27, // C
		28, 29, // D1
		30, 31, // D2
		18, 23, // E
		 4,  9, // F
		14, 15, // G1
		16, 17, // G2
		 5, 10, // H
		 6, 11, // I
		 7, 12, // J
		21, 26, // K
		20, 25, // L
		19, 24  // M
	};

	private void generateVertices() {
		vertices = new float[64];

		float[] xCoordsHoriz    = {0.25f, 2.25f, 2.75f, 4.75f};
		float[] xCoordsFarDiag  = {0.0f, 0.25f, 2.5f, 4.75f, 5.0f};
		float[] xCoordsNearDiag = {0.0f, 2.25f, 2.5f, 2.75f, 5.0f};
		float[] yCoords         = {0.0f, 0.5f, 3.25f, 3.5f, 3.75f, 6.5f, 7.0f};

		int index = 0;
		int i;
		// Top horizontal segments.
		for (i = 0; i < xCoordsHoriz.length; i++) {
			vertices[index++] = xCoordsHoriz[i];
			vertices[index++] = yCoords[0];
		}

		// Far/distal vertices of top vertical and diagonal segments.
		for (i = 0; i < xCoordsFarDiag.length; i++) {
			vertices[index++] = xCoordsFarDiag[i];
			vertices[index++] = yCoords[1];
		}

		// Near/proximal vertices of top vertical and diagonal segments.
		for (i = 0; i < xCoordsNearDiag.length; i++) {
			vertices[index++] = xCoordsNearDiag[i];
			vertices[index++] = yCoords[2];
		}

		// Middle horizontal segments.
		for (i = 0; i < xCoordsHoriz.length; i++) {
			vertices[index++] = xCoordsHoriz[i];
			vertices[index++] = yCoords[3];
		}

		// Near/proximal vertices of bottom vertical and diagonal segments.
		for (i = 0; i < xCoordsNearDiag.length; i++) {
			vertices[index++] = xCoordsNearDiag[i];
			vertices[index++] = yCoords[4];
		}

		// Far/distal vertices of bottom vertical and diagonal segments.
		for (i = 0; i < xCoordsFarDiag.length; i++) {
			vertices[index++] = xCoordsFarDiag[i];
			vertices[index++] = yCoords[5];
		}

		// Bottom horizontal segments.
		for (i = 0; i < xCoordsHoriz.length; i++) {
			vertices[index++] = xCoordsHoriz[i];
			vertices[index++] = yCoords[6];
		}
	}

	// Segment states.
	// We use ints instead of booleans for readability.
	private static final int[] ON  = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] OFF = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

	private static final int[] NUM_0 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_1 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_2 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_3 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_4 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_5 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_6 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_7 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_8 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] NUM_9 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] A     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] B     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] C     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] D     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] E     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] F     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] G     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] H     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] I     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] J     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] K     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] L     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] M     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] N     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] O     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] P     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] Q     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] R     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] S     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] T     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] U     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] V     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] W     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] X     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] Y     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static final int[] Z     = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

	public TextMeshActor(Character character, float x, float y, Color color, float width, float height) {
		super();

		setChar(character);
		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);

		createMesh();
	}

	public Character getChar() {
		return character;
	}

	public void setChar(Character character) {
		this.character = character;
	}

	private void createMesh() {
		if (vertices == null) {
			generateVertices();
		}

		switch (getChar()) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'A':
			case 'B':
			case 'C':
			case 'D':
			case 'E':
			case 'F':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'L':
			case 'M':
			case 'N':
			case 'O':
			case 'P':
			case 'Q':
			case 'R':
			case 'S':
			case 'T':
			case 'U':
			case 'V':
			case 'W':
			case 'X':
			case 'Y':
			case 'Z':
				setIndices(generateIndices(ON));
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

}
