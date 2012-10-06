package org.capstone.game;

import org.capstone.game.terrain.SimplexNoise;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
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
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		// Allocate terrain data with 4 bytes per pixel (RGB888).
		byte[] terrainData = new byte[(int) (4 * w * h)];
		SimplexNoise noise = new SimplexNoise();
		float[][] terrainNoise = noise.noise2D(512, 512);
//		Pixmap terrain = new Pixmap(terrainData, 0, (int) (4 * w * h));

		Pixmap terrain = new Pixmap(512, 512, Pixmap.Format.RGBA8888 );
		int width = 512;
		int height = 512;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				terrain.drawPixel(i, j, Color.rgba8888(terrainNoise[i][j], terrainNoise[i][j], terrainNoise[i][j], 1.0f));
			}
		}
//		terrain.setColor(1.0f);
//		terrain.fill();
		
		texture = new Texture(terrain);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 512);
		
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
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
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
