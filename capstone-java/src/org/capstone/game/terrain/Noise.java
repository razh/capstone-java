package org.capstone.game.terrain;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

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

	// Interpolation does not work as there is no fractional component.
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

		return (float) Interpolate.cosine(i0, i1, yfrac);
	}

	public static float[][] smoothNoise2D(int width, int height, int numOctaves,
	                                      float seedX, float seedY,
	                                      float persistence) {
		octaves = new float[numOctaves][width][height];
		m_numOctaves = numOctaves;
		m_width = width;
		m_height = height;

		// Populate each octave layer with noise.
		double totalAmplitude = 0.0;

		double frequency;
		double amplitude;

		for (int octave = 0; octave < numOctaves; octave++) {
			frequency = Math.pow(2, octave);
			amplitude = Math.pow(persistence, numOctaves - octave - 1);
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					octaves[octave][i][j] = (float) (amplitude * 0.5f * (1.0f +
					                        SimplexNoise.noise(i / frequency + seedX,
					                                           j / frequency + seedY)));
				}
			}
		}

		// Sum all layers up.
		float[][] noise = new float[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// Smooth noise is important to the visual style.
				for (int octave = 0; octave < numOctaves; octave++) {
					noise[i][j] += smoothNoise(i, j, octave);
				}

				noise[i][j] /= totalAmplitude;
			}
		}

		return noise;
	}

	private static float[] getNormals(float[][] map, int width, int height) {
		float[] normals = new float[width * height * 3];

		// Indices of each triangle.
		int index0, index1, index2;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				index0 = height * i + j;
				index1 = height * i + (j + 1);
				index2 = height * (i + 1) + j;

				// float[] vertex = {map[index0], map[index0]}
			}
		}
		return normals;
	}

	public static float[] flatSmoothNoise2D(int width, int height, int numOctaves,
	                                      float seedX, float seedY,
	                                      float persistence) {
		float[][] noise = Noise.smoothNoise2D(width, height, numOctaves,
		                                      seedX, seedY, persistence);

		float[] flatNoise = new float[width * height * 6];

		Random random = new Random();
		int index;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				index = 6 * (height * i + j);

				// Vertices.
				flatNoise[index + 0] = i / (float) width;
				flatNoise[index + 1] = j / (float) height;
				flatNoise[index + 2] = noise[i][j] * 2;

				// Normals.
				flatNoise[index + 3] = random.nextFloat();
				flatNoise[index + 4] = random.nextFloat();
				flatNoise[index + 5] = random.nextFloat();
			}
		}

		return flatNoise;
	}

	// Mesh test. Move this to its own class.
	// TODO: Imported classes: Mesh, VertexAttribute, Usage.
	public static Mesh getMesh(int width, int height) {
		float[] noise = Noise.flatSmoothNoise2D(width, height, 8, 0.0f, 0.0f, 0.5f);

		Mesh mesh = new Mesh(Mesh.VertexDataType.VertexBufferObject,
		                     true, width * height * 4, (width - 1) * height * 2,
		                     new VertexAttribute(Usage.Position, 3,
		                                         ShaderProgram.POSITION_ATTRIBUTE),
		                     new VertexAttribute(Usage.Normal, 3,
		                                         ShaderProgram.NORMAL_ATTRIBUTE));

		mesh.setVertices(noise);

		short[] indices = new short[(width - 1) * height * 2];
		int index = 0;
		for (int i = 0; i < width - 1; i++) {
			if (i % 2 == 0) {
				for (int j = 0; j < height; j++) {
					// index = 2 * (height * i + j);
					// First triangle.
					indices[index++] = (short) (height * i + j);
					indices[index++] = (short) ((short) height * (i + 1) + j);

					// indices[index + 1] = (short) ((short) i * j + j + 1);
					// indices[index + 0 + 2] = (short) ((short) (i + 1) * j + j);

					// // Second triangle.
					// indices[index + 3 + 0] = (short) ((short) i * j * j + 1);
					// indices[index + 3 + 1] = (short) ((short) (i + 1) * j + j + 1);
					// indices[index + 3 + 2] = (short) ((short) (i + 1) * j + j);

					// // First triangle lines.
					// indices[index + 0 + 0] = (short) (i * j + j);
					// indices[index + 0 + 1] = (short) ((short) i * j + j + 1);
					// indices[index + 0 + 2] = (short) ((short) i * j + j + 1);
					// indices[index + 0 + 3] = (short) ((short) (i + 1) * j + j);
					// indices[index + 0 + 4] = (short) ((short) (i + 1) * j + j);
					// indices[index + 0 + 5] = (short) (i * j + j);

					// // Second triangle lines.
					// indices[index + 6 + 0] = (short) ((short) i * j * j + 1);
					// indices[index + 6 + 1] = (short) ((short) (i + 1) * j + j + 1);
					// indices[index + 6 + 2] = (short) ((short) (i + 1) * j + j + 1);
					// indices[index + 6 + 3] = (short) ((short) (i + 1) * j + j);
					// indices[index + 6 + 4] = (short) ((short) (i + 1) * j + j);
					// indices[index + 6 + 5] = (short) ((short) i * j * j + 1);
				}
			} else {
				for (int j = height - 1; j >= 0; j-- ) {
					// index = 2 * (height * i + j);
					// First triangle.
					indices[index++] = (short) (height * i + j);
					indices[index++] = (short) ((short) height * (i + 1) + j);
				}
			}
		}
		mesh.setIndices(indices);

		for (int i = 0; i < 18; i++) {
		// for (int i = 0; i < width*height*3; i++) {
			System.out.print(noise[i] + "\t");
			if ((i+1)%3 == 0)
				System.out.print("\n");
		}

		return mesh;
	}
}
