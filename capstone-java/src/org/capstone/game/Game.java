package org.capstone.game;

import org.capstone.game.entities.Character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Game implements ApplicationListener {
	private State state;
	private FrameBuffer frameBuffer;
	private FrameBuffer frameBuffer2;
	private SpriteBatch spriteBatch;
	private TextureRegion fboRegion;
	private TextureRegion fboRegion2;
	private int frameBufferSize = 512;
	private int frameBuffer2Size = 512;
	private FPSLogger fpsLogger = new FPSLogger();

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

	private ShaderProgram verticalBlurShader;

	// http://www.gamerendering.com/2008/10/11/gaussian-blur-filter-shader/
	private String vertBlurVertexShader =
		"uniform mat4 projection;\n" +
		"uniform vec2 translate;\n" +
		"uniform vec2 scale;\n" +
		"attribute vec2 a_position;\n" +
		// "varying vec2 vUv;\n" +

		// "attribute vec2 uv;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		// "  vUv = ;\n" +
		"  vec2 position = a_position;\n" +
		"  position.x = position.x * scale.x + translate.x;\n" +
		"  position.y = position.y * scale.y + translate.y;\n" +
		"  gl_Position = projection * vec4(position.xy, 0.0, 1.0);\n" +
		// "  vUv = gl_Position.xy;\n" +

		// "  gl_Position = projection * vec4(position.xy, 0.0, 1.0);\n" +

		"}";

	private String vertBlurFragmentShader =
		"#ifdef GL_ES\n" +
		"precision mediump float;\n" +
		"#endif\n" +
		"\n" +
		"uniform sampler2D fboTexture;\n" +
		"uniform float v;\n" +
		"uniform vec4 v_color;\n" +
		"\n" +
		// "varying vec2 vUv;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  vec4 sum = vec4(0.0);\n" +
		"  vec4 color = v * v_color;\n" +
		"  vec2 vUv = gl_FragCoord.xy;\n" +
		"  \n" +
		"  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y));\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y - 4.0 * v)) * 0.051;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y - 3.0 * v)) * 0.0918;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y - 2.0 * v)) * 0.12245;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y - 1.0 * v)) * 0.1531;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y)) * 0.1633;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y + 1.0 * v)) * 0.1531;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y + 2.0 * v)) * 0.12245;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y + 3.0 * v)) * 0.0918;\n" +
		// "  sum += texture2D(fboTexture, vec2(vUv.x, vUv.y + 4.0 * v)) * 0.051;\n" +
		"  \n" +
		"  gl_FragColor = normalize(v_color * sum);\n" +
		"}";

	private String batchVertexShader =
		"attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
		"attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
		"attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
		"uniform mat4 u_projTrans;\n" +
		"varying vec4 v_color;\n" +
		"varying vec2 v_texCoords;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
		"  v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
		"  gl_Position = u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
		"}\n";

	private String batchFragmentShader =
		"#ifdef GL_ES\n" +
		"#define LOWP lowp\n" +
		"precision mediump float;\n" +
		"#else\n" +
		"#define LOWP \n" +
		"#endif\n" +
		"varying LOWP vec4 v_color;\n" +
		"varying vec2 v_texCoords;\n" +
		"uniform float v;\n" +
		"uniform sampler2D u_texture;\n" +
		"uniform sampler2D u_texture2;\n" +
		"void main()\n" +
		"{\n" +
		"  vec4 sum = vec4(0.0);\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 4.0 * v)) * 0.051;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 3.0 * v)) * 0.0918;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 2.0 * v)) * 0.12245;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 1.0 * v)) * 0.1531;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y)) * 0.1633;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 1.0 * v)) * 0.1531;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 2.0 * v)) * 0.12245;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 3.0 * v)) * 0.0918;\n" +
		"  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 4.0 * v)) * 0.051;\n" +
		"  gl_FragColor = v_color * mix(sum, texture2D(u_texture2, v_texCoords), 0.2);\n" +
		"}";

	private ShaderProgram batchShader;

	@Override
	public void create() {
		float width  = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		Gdx.graphics.setVSync(true);

		System.out.println("GL20: " + Gdx.graphics.isGL20Available());
		System.out.println(vertexShader);
		System.out.println(fragmentShader);
		System.out.println("---------");
		System.out.println(vertBlurVertexShader);
		System.out.println(vertBlurFragmentShader);
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		verticalBlurShader = new ShaderProgram(vertBlurVertexShader, vertBlurFragmentShader);
		batchShader = new ShaderProgram(batchVertexShader, batchFragmentShader);
		spriteBatch = new SpriteBatch();

		frameBuffer = new FrameBuffer(Format.RGBA4444, frameBufferSize, frameBufferSize, true);
		fboRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		fboRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fboRegion.flip(false, true);

		frameBuffer2 = new FrameBuffer(Format.RGBA4444, frameBuffer2Size, frameBuffer2Size, true);
		fboRegion2 = new TextureRegion(frameBuffer2.getColorBufferTexture());
		fboRegion2.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fboRegion2.flip(false, true);

		System.out.println("Compiled: " + shaderProgram.isCompiled() + "---------");
		System.out.println("Compiled (vB): " + verticalBlurShader.isCompiled() + "---------");
		System.out.println("Compiled (b): " + batchShader.isCompiled() + "---------");

		Character circle = new Character(100, 200, new Color(0.173f, 0.204f, 0.220f, 1.0f), 30);
		circle.setVelocity(200.0f, 100.0f);

		state = new State(width, height);
		state.getStage().setShaderProgram(shaderProgram);
		state.getStage().addActor(circle);

		// Invisible cursor.
		// Gdx.input.setCursorCatched(true);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void dispose() {
		shaderProgram.dispose();
		frameBuffer.dispose();
	}

	@Override
	public void render() {
		handleInput();
		state.update();

		// FrameBuffer1
//		Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		// frameBuffer.begin();

		Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		// Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// state.getStage().setShaderProgram(shaderProgram);
		state.getStage().draw();

		// frameBuffer.end();

		// FrameBuffer2
		// frameBuffer2.begin();

		// Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// state.getStage().draw();

		// frameBuffer2.end();

		//------ DON'T KNOW ABOUT THIS
		// Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		// fboRegion.getTexture().bind(0); // Binds it to texture 0.
		// Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// state.getStage().setShaderProgram(verticalBlurShader);
		// verticalBlurShader.setUniformf("v", 1.0f / 512.0f);
		// verticalBlurShader.setUniformi("fboTexture", 0);
		// // Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		// state.getStage().draw();

		//------ THIS WORKS
		// fboRegion2.getTexture().bind(1);

		// spriteBatch.begin();
		// spriteBatch.setShader(batchShader);
		// batchShader.setUniformi("u_texture2", 1);
		// batchShader.setUniformf("v", 1 / frameBufferSize);
		// Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// spriteBatch.draw(fboRegion, 0, 0, State.getWidth(), State.getHeight());
		// spriteBatch.end();
		fpsLogger.log();
	}

	private void handleInput() {
		if (state.getStage().getRoot()!= null) {
//			state.getStage().getRoot().getChildren().get(0).setPosition(Gdx.input.getX(), -Gdx.input.getY() + State.getHeight());
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			((Character) state.getStage().getRoot().getChildren().get(0)).takeFire();
			// System.out.println( camera.combined.toString() );
//			System.out.println( state.getCharacters().get(0).physics.getPosition().toString());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {}
	}

	@Override
	public void resize(int width, int height) {
//		State.setWidth(width);
//		State.setHeight(height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
