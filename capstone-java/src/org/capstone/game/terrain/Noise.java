package org.capstone.game.terrain;

public class Noise {
  // private static float[][][] octaves;

	public static float smoothNoise(int x, int y, int numOctaves) {
    double persistence = 0.5;
    double totalAmplitude = 0.0;

    float total = 0.0f;
    double frequency;
    double amplitude;
    for (int i = 0; i < numOctaves; i++) {
      frequency = Math.pow(2, i);
      amplitude = Math.pow(persistence, i);
      totalAmplitude += amplitude;

      total += SimplexNoise.noise(x * frequency, y * frequency) * amplitude;
    }


    totalAmplitude = 1 / totalAmplitude;

    return (float) ((total * totalAmplitude + 1.0f) / 2.0f);
  }

  public static float[][] smoothNoise2D(int width, int height) {
    // octaves = new float[numOctaves][width][height];

    float[][] noise = new float[width][height];
    float min = Float.MAX_VALUE;
    float max = Float.MIN_VALUE;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++ ) {
        // noise[i][j] = (float) ((SimplexNoise.noise(i/32, j/32) + 1.0f) / 2.0f);
        // noise[i][j] = (float) (i * j) / (width * height);
        noise[i][j] = smoothNoise(i, j, 10);
       if (noise[i][j] < min)
          min = noise[i][j];
       if (noise[i][j] > max)
          max = noise[i][j];
      }
    }
    System.out.println( min + " " + max);

    return noise;
  }
}
