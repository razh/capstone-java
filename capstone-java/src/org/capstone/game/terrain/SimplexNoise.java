package org.capstone.game.terrain;

import java.util.Random;

// http://webstaff.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
public class SimplexNoise implements Noise {
	Random random = new Random();

	private static int grad3[][] = {
		{ 1,  1,  0},
		{-1,  1,  0},
		{ 1, -1,  0},
		{-1, -1,  0},
		{ 1,  0,  1},
		{-1,  0,  1},
		{ 1,  0, -1},
		{-1,  0, -1},
		{ 0,  1,  1},
		{ 0, -1,  1},
		{ 0,  1, -1},
		{ 0, -1, -1}
	};

	private static int p[] = {
		151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140,
		36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120,
		234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
		88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71,
		134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
		230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161,
		1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130,
		116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
		124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227,
		47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44,
		154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98,
		108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34,
		242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14,
		239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121,
		50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
		141, 128, 195, 78, 66, 215, 61, 156, 180
	};

	private static int perm[] = new int[512];
	static {
		for (int i = 0; i < 512; i++)
			perm[i] = p[i & 255];
	}

	private static int fastFloor(double x) {
		return x > 0 ? (int) x: (int) x - 1;
	}

	private static double dot(int g[], double x, double y) {
		return g[0] * x + g[1] * y;
	}

	private static double noise( double xin, double yin ) {
		double n0, n1, n2; // Noise contributions from the three corners.

		// Skew the input space to determine which simplex cell we're in.
		final double f2 = 0.5 * (Math.sqrt(3.0) - 1.0);

		// Hairy factor for 2D.
		double s = (xin + yin) * f2;
		int i = fastFloor(xin + s);
		int j = fastFloor(yin + s);

		final double g2 = (3.0 - Math.sqrt(3.0)) / 6.0;
		double t = (i + j) * g2;

		// Unskew the cell origin back to (x, y) space.
		double X0 = i - t;
		double Y0 = j - t;

		// The x, y distances from the cell origin.
		double x0 = xin - X0;
		double y0 = yin - Y0;

		// For the 2D case, the simplex shape is an equilateral triangle.
		// Determine which simplex we are in.

		// Offsets for second (middle) corner of simplex in (i, j) coords.
		int i1, j1;
		// Lower triangle, XY order: (0, 0) -> (1, 0) -> (1, 1).
		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		}
		// Upper triangle, YX order: (0, 0) -> (0, 1) -> (1, 1);
		else {
			i1 = 0;
			j1 = 1;
		}

		// A step of (1, 0) in (i, j) means a step of (1 - c, -c) in (x, y), and
		// a step of (0, 1) in (i, j) means a step of (-c, 1 - c) in (x, y), where
		// c = (3 - sqrt(3)) / 6.

		// Offsets for middle corner in (x, y) unskewed coords.
		double x1 = x0 - i1 + g2;
	}

	public float[][] noise2D(int width, int height) {
		float[][] map = new float[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				map[i][j] = random.nextFloat();
			}
		}

		return map;
	}
}
