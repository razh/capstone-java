package org.capstone.game;

import org.capstone.game.graphics.CircleGraphicsComponent;
import org.capstone.game.graphics.RectGraphicsComponent;
import org.capstone.game.entities.Character;

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

	private float width;
	private float height;
	private State state;

	private String vertexShader =
		"uniform mat4 projection;\n" +
		"uniform vec2 translate;\n" +
		"uniform vec2 scale;\n" +
		"attribute vec2 a_position;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  vec2 position = a_position;\n" +
		"  position.x = position.x * scale.x + translate.x;\n" +
		"  position.y = position.y * scale.y + translate.y;\n" +
		"  gl_Position = projection * vec4(position.xy, 0.0, 1.0);\n" +
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
		this.width  = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1.0f, this.height / this.width);
		camera.zoom = this.width;
		camera.update();

		System.out.println( camera.combined.toString() );

		System.out.println(vertexShader);
		System.out.println(fragmentShader);
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		System.out.println(shaderProgram.isCompiled());

		state = new State();
		state.addCharacter(new Character(100.0f, 0.0f, new Color(1.0f, 0.0f, 0.0f, 1.0f), 10.0f));
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
	}

	@Override
	public void render() {
		handleInput();
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		shaderProgram.begin();
		shaderProgram.setUniformMatrix("projection", camera.combined);
		state.getCharacters().get(0).render(shaderProgram);
		shaderProgram.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0.0f, 0.1f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-0.1f, 0.0f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(0.1f, 0.0f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0.0f, -0.1f);
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			System.out.println( camera.combined.toString() );
		}
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {
			camera.zoom += 1.0f;
			camera.update();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {
			camera.zoom -= 1.0f;
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
