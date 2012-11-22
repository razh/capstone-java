package org.capstone.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;

public class TextMeshActor extends MeshActor {
	protected Mesh mesh;
	protected Character character;
	protected static float[] vertices = new float[24];

	/*
		For a sixteen-segment display:

		     A1     A2
		   ------ ------
		  | \    |    / |
		F | H \ J|  / K | B
		  | G1  \|/  G2 |
		   ------ ------
		  |     /|\     |
		E | N / M|  \ L | C
		  | /    |    \ |
		   ------ ------
		     D1     D2

		Each of these segments is constructed from two vertices:

		    0--1   2--3
		  4      5      6
		  |      |      |
		  7      8      9
		    A--B   C--D
		  E      F      G
		  |      |      |
		  H      I      J
		    K--L   M--N

		where A-M represent 10-24.
	*/

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
		switch (character) {
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

				break;
		}
	}

	private void generateVertices() {
		// Coordinates for the horizontal segments of the display.
		float[] xCoordsX = {1.0f, 2.0f, 3.0f, 4.0f};
		float[] xCoordsY = {0.5f, 3.5f, 6.5f};
		// Coordinates for the vertical segments of the display.
		float[] yCoordsX = {0.5f, 2.5f, 4.0f};
		float[] yCoordsY = {1.0f, 3.0f, 5.0f, 6.0f};

		int index = 0;
		int i;
		// Top horizontal segments.
		for (i = 0; i < xCoordsX.length; i++) {
			vertices[index++] = xCoordsX[i];
			vertices[index++] = xCoordsY[0];
		}

		// Top vertical segments.
		for (i = 0; i < yCoordsX.length; i++) {
			vertices[index++] = yCoordsX[i];
			vertices[index++] = yCoordsY[0];
		}
		for (i = 0; i < yCoordsX.length; i++) {
			vertices[index++] = yCoordsX[i];
			vertices[index++] = yCoordsY[1];
		}

		// Middle horizontal segments.
		for (i = 0; i < xCoordsX.length; i++) {
			vertices[index++] = xCoordsX[i];
			vertices[index++] = xCoordsY[1];
		}

		// Bottom horizontal segments.
		for (i = 0; i < yCoordsX.length; i++) {
			vertices[index++] = yCoordsX[i];
			vertices[index++] = yCoordsY[2];
		}
		for (i = 0; i < yCoordsX.length; i++) {
			vertices[index++] = yCoordsX[i];
			vertices[index++] = yCoordsY[3];
		}

		// Bottom horizontal segments.
		for (i = 0; i < xCoordsX.length; i++) {
			vertices[index++] = xCoordsX[i];
			vertices[index++] = xCoordsY[2];
		}
	}

	private short[] getIndices(float[] segmentStates) {
		// Loop through once to get the number of segments we need to draw.
		int numSegments = 0;
		for (int i = 0; i < segmentStates.length; i++) {
			if (segmentStates[i] == 1)
				numSegments++;
		}

		short[] indices = new short[numSegments];
		int index = 0;
		for (int i = 0; i < segmentStates.length; i++) {
			if (segmentStates[i] == 1) {
				indices[index] = 0;
			}
		}

		return indices;
	}
}
