package org.capstone.game;

import org.capstone.game.terrain.Noise;
import org.capstone.game.terrain.SimplexNoise;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
//import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Game implements ApplicationListener {
//	 private OrthographicCamera camera;
	private PerspectiveCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;

	private ShaderProgram shader;
	private Mesh mesh;
	private Mesh mesh2;

	private static float mouseX;
	private static float mouseY;

	private String vertexShader =
		"attribute vec4 a_position;\n" +
		"uniform mat4 projectionMatrix;\n" +
		"void main()\n" +
		"{\n" +
		"  gl_Position = a_position * projectionMatrix;\n" +
		"}";

	private String fragmentShader =
		"void main()\n" +
		"{\n" +
		"  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
		"}";

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		 //camera = new OrthographicCamera(1, h / w);
		camera = new PerspectiveCamera(60, w, h);
		camera.position.set(0, 0, 5);
		camera.update();
		batch = new SpriteBatch();

		int width = 4;
		int height = 4;
		Pixmap terrain = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		float[][] noise = Noise.smoothNoise2D(width, height, 5, 0.0f, 0.0f, 0.5f);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int c = Color.rgba8888(noise[i][j], noise[i][j], noise[i][j], 1.0f);
				terrain.drawPixel(i, j, c);
			}
		}

		texture = new Texture(terrain);
		// texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// TextureRegion region = new TextureRegion(texture, 0, 0, width, height);

		// sprite = new Sprite(region);
		// sprite.setSize(1.0f, 1.0f * sprite.getHeight() / sprite.getWidth());
		// sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		// sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);

		System.out.println("GL20: " + Gdx.graphics.isGL20Available());
		
		shader = new ShaderProgram(vertexShader, fragmentShader);
		mesh = Noise.getMesh(width, height);
//		mesh.bind(shader);
		System.out.println("HELLO: " + mesh.getNumIndices());

		mesh2 = new Mesh(true, 3, 3,
		        new VertexAttribute(Usage.Position, 3,
		                            ShaderProgram.POSITION_ATTRIBUTE),
		        new VertexAttribute(Usage.Normal, 3,
		                            ShaderProgram.NORMAL_ATTRIBUTE),
		        new VertexAttribute(Usage.ColorPacked, 4,
		                            ShaderProgram.COLOR_ATTRIBUTE));

		System.out.println(ShaderProgram.NORMAL_ATTRIBUTE);
		mesh2.setVertices(new float[] { -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, Color.toFloatBits( 1.0f, 0.0f, 0.0f, 1.0f),
		                                 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, Color.toFloatBits( 1.0f, 0.0f, 0.0f, 1.0f),
		                                 0.0f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f, Color.toFloatBits( 1.0f, 0.0f, 0.0f, 1.0f)});
		mesh2.setIndices(new short[] { 0, 1, 2 });

		camera.near = 0.01f;
		camera.far = 1000.0f;

//		GL10 gl = Gdx.graphics.getGL10();
//		gl.glEnable(GL10.GL_LIGHTING);
//		// gl.glLightModelfv(gl.GL_LIGHT_MODEL_AMBIENT, new float[]{0.9f, 0.9f, 0.2f, 1.0f}, 0);
//		gl.glEnable(GL10.GL_LIGHT0);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[]{0.0f, 0.0f, 2.0f, 1.0f}, 0);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[]{0.9f, 0.9f, 0.9f, 1.0f}, 0);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[]{0.9f, 0.5f, 0.9f, 1.0f}, 0);
//		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, new float[]{0.9f, 0.9f, 0.9f, 1.0f}, 0);

		// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[] {1.0f, 1.0f, 0.5f, 1.0f}, 0);
		// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0);
		// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0);
		// gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 30.0f);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {
		boolean isTouched = Gdx.input.isTouched();

		if (isTouched) {
			mouseX += Gdx.input.getDeltaX();
			mouseY -= Gdx.input.getDeltaY();
//			System.out.println(Gdx.input.getDeltaX());
			 camera.translate(Gdx.input.getDeltaX() / (float) Gdx.graphics.getWidth(),
			                  Gdx.input.getDeltaY() / (float) Gdx.graphics.getHeight(), 0.0f);

			// int width = 512;
			// int height = 512;
			// Pixmap terrain = new Pixmap(width, height, Pixmap.Format.RGBA8888);
			// float[][] noise = Noise.smoothNoise2D(width, height, 8, mouseX, mouseY, 0.5f);

			// for (int i = 0; i < width; i++) {
			// 	for (int j = 0; j < height; j++) {
			// 		int c = Color.rgba8888(noise[i][j], noise[i][j], noise[i][j], 1.0f);
			// 		terrain.drawPixel(i, j, c);
			// 	}
			// }

			// texture = new Texture(terrain);
			// texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			// TextureRegion region = new TextureRegion(texture, 0, 0, width, height);

			// sprite = new Sprite(region);
			// sprite.setSize(1.0f, 1.0f * sprite.getHeight() / sprite.getWidth());
			// sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			// sprite.setPosition(-sprite.getWidth() / 2 + mouseX / Gdx.graphics.getWidth(),
			//                    -sprite.getHeight() / 2 + mouseY / Gdx.graphics.getHeight());
		}

		boolean isWPressed = Gdx.input.isKeyPressed(Keys.W);
		boolean isSPRessed = Gdx.input.isKeyPressed(Keys.S);

		if (isWPressed) {
			camera.translate(0.0f, 0.0f, -0.1f);
		}
		if (isSPRessed) {
			camera.translate(0.0f, 0.0f, 0.1f);
		}
		camera.update();

		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		shader.begin();
		shader.setUniformMatrix("projectionMatrix", camera.combined);
		// mesh.bind(shader);
//		 mesh.render(GL10.GL_TRIANGLE_STRIP);
//		mesh.render(shader, GL20.GL_LINES);

		 mesh.render(shader, GL20.GL_TRIANGLE_STRIP);
//		 mesh2.render(GL10.GL_TRIANGLES);
		 mesh2.render(shader, GL20.GL_TRIANGLES);
		shader.end();
		// sprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
