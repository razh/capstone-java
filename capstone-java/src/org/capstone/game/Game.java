package org.capstone.game;

import org.capstone.game.graphics.CircleGraphicsComponent;
import org.capstone.game.graphics.RectGraphicsComponent;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Game implements ApplicationListener {
	private OrthographicCamera camera;
	// private PerspectiveCamera camera;
	// private SpriteBatch batch;
	// private Texture texture;
	// private Sprite sprite;
	private float width;
	private float height;
	private RectGraphicsComponent rect;
	private CircleGraphicsComponent circle;

	private String vertexShader =
		"uniform mat4 projection;\n" +
		"uniform vec2 translate;\n" +
		"uniform vec2 scale;\n" +
		"attribute vec2 a_position;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		// "  gl_Position = projection * vec4(a_position.xy, 0.0, 1.0);\n" +
		// "  vec2 position = vec2(a_position.x, a_position.y);\n" +
		// "  vec2 position = vec2(a_position.x, a_position.y);\n" +
		"  vec2 position = a_position;\n" +
		"  position.x = position.x * scale.x + translate.x;\n" +
		"  position.y = position.y * scale.y + translate.y;\n" +
		"  gl_Position = projection * vec4(position.xy, 0.0, 1.0);\n" +
		// "  gl_Position = projection * vec4(a_position.x * scale.x + translate.x, a_position.y * scale.y + translate.y, 0.0, 1.0);\n" +
		// "  gl_Position = position;\n" +
		// "  gl_Position = vec4(position.xy, 0.0, position.w);" +
		"}";

	private String fragmentShader =
		"#ifdef GL_ES\n" +
		"precision medp float;\n" +
		"#endif\n" +
		"uniform vec4 v_color;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  gl_FragColor = v_color;\n" +
		"}";

	private ShaderProgram shaderProgram;

	@Override
	public void create() {
		// float w = Gdx.graphics.getWidth();
		// float h = Gdx.graphics.getHeight();

		this.width  = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1.0f, this.height / this.width);
		// camera.setToOrtho(false);
		camera.zoom = -this.width;
		camera.update();
		// camera = new OrthographicCamera(this.width, this.height);

		System.out.println( camera.combined.toString() );
		// batch = new SpriteBatch();

		// texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		// texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);

		// sprite = new Sprite(region);
		// sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		// sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		// sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		System.out.println(vertexShader);
		System.out.println(fragmentShader);
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		System.out.println(shaderProgram.isCompiled());
		// rect = new RectGraphicsComponent(0, 0, new Color(1.0f, 0.0f, 0.0f, 1.0f), 10, 30);
		// rect.init(2,4);
		circle = new CircleGraphicsComponent(0, 0, new Color(1.0f, 0.0f, 0.0f, 1.0f), 10);
		circle.init();
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
		// batch.dispose();
		// texture.dispose();
	}

	@Override
	public void render() {
		handleInput();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		shaderProgram.begin();
		shaderProgram.setUniformMatrix("projection", camera.combined);
		// rect.render(shaderProgram);
		circle.render(shaderProgram);
		shaderProgram.end();

		// batch.setProjectionMatrix(camera.combined);
		// batch.begin();
		// sprite.draw(batch);
		// batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0.0f, 0.01f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-0.01f, 0.0f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(0.01f, 0.0f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0.0f, -0.01f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			System.out.println( camera.combined.toString() );
		}
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {
			camera.zoom += 0.01;
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {
			camera.zoom -= 0.01;
			camera.update();
		}
	}

	@Override
	public void resize(int width, int height) {
		this.width  = (float) width;
		this.height = (float) height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
