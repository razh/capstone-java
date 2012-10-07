package org.capstone.game.terrain;

public class Noise {
	private static int m_numOctaves;
	private static int m_width;
	private static int m_height;
	private static float[][][] octaves;

	private static float smoothNoise(int x, int y, int octave) {
		int x0 = (x - 1) % m_width;
		int x1 = (x + 1) % m_width;
		int y0 = (y - 1) % m_height;
		int y1 = (y + 1) % m_height;

		if (x0 < 0) x0 += m_width;
		if (x1 < 0) x1 += m_width;
		if (y0 < 0) y0 += m_height;
		if (y1 < 0) y1 += m_height;

		// System.out.println("x0: " + x0 + ", x1: " + x1 + ", y0: " + y0 + ", y1: " + y1 );
		float corners = (octaves[octave][x0][y0] +
		                 octaves[octave][x1][y0] +
		                 octaves[octave][x0][y1] +
		                 octaves[octave][x1][y1]) / 16.0f;
		float sides = (octaves[octave][x0][y] +
		               octaves[octave][x1][y] +
		               octaves[octave][x][y0] +
		               octaves[octave][x][y1]) / 8.0f;
		float center = octaves[octave][x][y] / 4.0f;

		return corners + sides + center;
	}

	static Boolean test = false;
	static Boolean p = false;

	private static float interpolatedNoise(float x, float y, int octave) {
		// Separate integer and fractional parts of x and y.
		int xint = (int) x;
		float xfrac = x - xint;

		int yint = (int) y;
		float yfrac = y - yint;

		float v0 = smoothNoise(xint, yint, octave);
		float v1 = smoothNoise((xint + 1) % m_width, yint, octave);
		float v2 = smoothNoise(xint, (yint + 1) % m_height, octave);
		float v3 = smoothNoise((xint + 1) % m_width, (yint + 1) % m_height, octave);

		float i0 = (float) Interpolate.cosine(v0, v1, xfrac);
		float i1 = (float) Interpolate.cosine(v2, v3, xfrac);
		if ( !test) {
			test = true;
			System.out.println(xfrac);
		}
		if ( xfrac > 0 ) {
			p = true;
		}

		return (float) Interpolate.cosine(i0, i1, yfrac);
	}

	public static float[][] smoothNoise2D(int width, int height, int numOctaves,
	                                      float seed) {
		octaves = new float[numOctaves][width][height];
		m_numOctaves = numOctaves;
		m_width = width;
		m_height = height;
		System.out.println( width + " " + height);

		// float total = 0.0f;

		// Populate each octave layer with noise.
		double persistence = 0.5;
		double totalAmplitude = 0.0;

		double frequency;
		double amplitude;
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for (int octave = 0; octave < numOctaves; octave++) {
			frequency = Math.pow(2, octave);
			amplitude = Math.pow(persistence, numOctaves - octave - 1);
			totalAmplitude += amplitude;

			System.out.println( "f: " + frequency + " a: " + amplitude);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					octaves[octave][i][j] = (float) (amplitude * 0.5f *
					                        (1.0f +
					                        SimplexNoise.noise(i / frequency + seed,
					                                           j / frequency + seed)));

					if (octaves[octave][i][j] < min)
						min = octaves[octave][i][j];
					if (octaves[octave][i][j] > max)
						max = octaves[octave][i][j];
				}
			}

			// totalAmplitude += max;

			System.out.println( "octave: " + octave + " " + min + " " + max);
			min = Float.MAX_VALUE;
			max = Float.MIN_VALUE;
		}


		float[][] noise = new float[width][height];

		float minb = Float.MAX_VALUE;
		float maxb = Float.MIN_VALUE;
		min = Float.MAX_VALUE;
		max = Float.MIN_VALUE;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
					// noise[i][j] = (float) ((SimplexNoise.noise(i/32, j/32) + 1.0f) / 2.0f);
					// noise[i][j] = (float) (i * j) / (width * height);
				for (int octave = 0; octave < numOctaves; octave++) {
					noise[i][j] += interpolatedNoise(i, j, octave);
				}
				if (noise[i][j] < minb)
					minb = noise[i][j];
				if (noise[i][j] > maxb)
					maxb = noise[i][j];

				noise[i][j] /= totalAmplitude;
				// noise[i][j] += 1.0f;

				if (noise[i][j] < min)
					min = noise[i][j];
				if (noise[i][j] > max)
					max = noise[i][j];

			}
		}

		// minb = Float.MAX_VALUE;
		// maxb = Float.MIN_VALUE;
		// for ( int i = 0; i < width; i++) {
		// 	for (int j = 0; j < height; j++ ) {
		// 		noise[i][j] /= max;

		// 						if (noise[i][j] < minb)
		// 			minb = noise[i][j];
		// 		if (noise[i][j] > maxb)
		// 			maxb = noise[i][j];

		// 	}
		// }
		System.out.println( min + " " + max);
		System.out.println( minb + " " + maxb);
		System.out.println("xfrac>0: " + p);
		System.out.println("amp: " + totalAmplitude);
		return noise;
	}
}
