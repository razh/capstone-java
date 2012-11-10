package org.capstone.game;

import org.capstone.game.entities.Character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import com.badlogic.gdx.scenes.scene2d.actions.*;

public class Game implements ApplicationListener {
	private OrthographicCamera camera;
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
		float width  = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		Gdx.graphics.setVSync(true);

		System.out.println("GL20: " + Gdx.graphics.isGL20Available());
		System.out.println(vertexShader);
		System.out.println(fragmentShader);
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		System.out.println("Compiled: " + shaderProgram.isCompiled() + "---------");

		CircleMeshActor circle = new CircleMeshActor();
		circle.width = 100;
		circle.height = 100;
		circle.x = 100;
		circle.y = 20;
		circle.setColor(new Color(0.173f, 0.204f, 0.220f, 1.0f));
		// circle.action(MoveTo.$(100, -200, 2000));
		
		state = new State(width, height);
		state.getStage().setShaderProgram(shaderProgram);
		state.getStage().addActor(circle);
		// state.getStage().addActor(new Character(100.0f, 0.0f, new Color(0.173f, 0.204f, 0.220f, 1.0f), 50.0f));
		// state.getStage().getActor(0).setVelocity(0.25f, 0.25f);
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
	}

	@Override
	public void render() {
		handleInput();
		state.update();
		// state.getCharacters().get(0).physics.setPosition(state.getCharacters().get(0).physics.getPosition().add(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY()));
		Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		state.getStage().draw();
		// state.getCharacters().get(0).render(shaderProgram);
	}

	private void handleInput() {
		state.getStage().root.getActors().get(0).x = Gdx.input.getX();
		state.getStage().root.getActors().get(0).y = -Gdx.input.getY() + state.getHeight();
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			// System.out.println( camera.combined.toString() );
//			System.out.println( state.getCharacters().get(0).physics.getPosition().toString());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {}
	}

	@Override
	public void resize(int width, int height) {
		State.setWidth(width);
		State.setHeight(height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
