package org.capstone.game;

import org.capstone.game.terrain.Noise;
import org.capstone.game.terrain.SimplexNoise;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Game implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;

	private static float mouseX;
	private static float mouseY;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();

		int width = 512;
		int height = 512;
		Pixmap terrain = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		float[][] noise = Noise.smoothNoise2D(width, height, 8, 0.0f, 0.0f, 0.5f);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int c = Color.rgba8888(noise[i][j], noise[i][j], noise[i][j], 1.0f);
				terrain.drawPixel(i, j, c);
			}
		}

		texture = new Texture(terrain);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, width, height);

		sprite = new Sprite(region);
		sprite.setSize(1.0f, 1.0f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
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

			sprite.setPosition(-sprite.getWidth() / 2 + mouseX / Gdx.graphics.getWidth(),
			                  -sprite.getHeight() / 2 + mouseY / Gdx.graphics.getHeight());
		}

		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
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
