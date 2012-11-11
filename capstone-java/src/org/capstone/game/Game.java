package org.capstone.game;

import org.capstone.game.entities.Character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

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

//		CircleMeshActor circle = new CircleMeshActor();
//		circle.setWidth(20);
//		circle.setHeight(20);
//		circle.setPosition(100, 200);
//		circle.setColor(new Color(0.173f, 0.204f, 0.220f, 1.0f));
		Character circle = new Character(100, 200, new Color(0.173f, 0.204f, 0.220f, 1.0f), 30);
		circle.addAction(
		  forever(
		    sequence(
		      parallel(
		        color(new Color(0.8f, 0.204f, 0.220f, 1.0f), 0.05f, Interpolation.pow3),
		        sizeBy(15, 15, 0.05f, Interpolation.pow3)
		      ),
		      parallel(
		        color(new Color(0.173f, 0.204f, 0.220f, 1.0f), 0.15f, Interpolation.linear),
		        sizeBy(-15, -15, 0.15f, Interpolation.linear)
		      )
		    )
		  )
		);

		state = new State(width, height);
		state.getStage().setShaderProgram(shaderProgram);
		state.getStage().addActor(circle);
		// state.getStage().addActor(new Character(100.0f, 0.0f, new Color(0.173f, 0.204f, 0.220f, 1.0f), 50.0f));
		// state.getStage().getActor(0).setVelocity(0.25f, 0.25f);
		
		// Invisible cursor.
		// Gdx.input.setCursorCatched(true);
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
	}

	@Override
	public void render() {
		handleInput();
		state.update();

		Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		state.getStage().draw();
	}

	private void handleInput() {
		if (state.getStage().getRoot()!= null) {
			state.getStage().getRoot().getChildren().get(0).setPosition(Gdx.input.getX(), -Gdx.input.getY() + State.getHeight());
		}

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
