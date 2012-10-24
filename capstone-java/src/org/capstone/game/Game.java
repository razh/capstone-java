package org.capstone.game;

import org.capstone.game.entities.Character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

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
		"precision mediump float;\n" +
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
		width  = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		Gdx.graphics.setVSync(true);

		camera = new OrthographicCamera(1.0f, height / width);
		camera.zoom = width;
		camera.update();

		System.out.println( camera.combined.toString() );

		System.out.println("GL20: " + Gdx.graphics.isGL20Available());
		System.out.println(vertexShader);
		System.out.println(fragmentShader);
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		System.out.println("Compiled: " + shaderProgram.isCompiled() + "---------");

		state = new State(width, height);
		state.addCharacter(new Character(100.0f, 0.0f, new Color(0.173f, 0.204f, 0.220f, 1.0f), 50.0f));
		state.getCharacters().get(0).physics.setVelocity(0.25f, 0.25f);
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
	}

	@Override
	public void render() {
		handleInput();
		state.update();
		state.getCharacters().get(0).physics.setPosition(state.getCharacters().get(0).physics.getPosition().add(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY()));
		Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
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
			// System.out.println( camera.combined.toString() );
			System.out.println( state.getCharacters().get(0).physics.getPosition().toString());
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
