package org.capstone.game;

import java.sql.*;

import org.capstone.game.TextMeshGroup.Alignment;
import org.capstone.game.files.LevelLoader;
import org.capstone.game.input.GameInputProcessor;
import org.capstone.game.network.Database;
import org.capstone.game.tests.ShapesStageTest;
import org.capstone.game.tests.SimpleGameStageTest;
import org.capstone.game.tests.StageTest;
import org.capstone.game.tests.TextStageTest;
import org.capstone.game.ui.Counter;
import org.capstone.game.ui.Scoreboard;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Game implements ApplicationListener {
	private FrameBuffer frameBuffer;
	private FrameBuffer frameBuffer2;
	private SpriteBatch spriteBatch;
	private TextureRegion fboRegion;
	private TextureRegion fboRegion2;
	private int frameBufferSize = 512;
	private FPSLogger fpsLogger = new FPSLogger();
//	private boolean running = true;
	private boolean gl20 = false;
	private boolean lost = false;

	private LevelLoader loader;
	private Level level;
	private Player player;

	private String vertexShader =
		"uniform mat4 projection;\n" +
		"uniform float rotation;\n" +
		"uniform vec2 translate;\n" +
		"uniform vec2 scale;\n" +
		"attribute vec2 a_position;\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"  vec2 position = vec2(0.0);\n" +
		"  if (rotation != 0.0) {\n" +
		"    float r_cos = cos(radians(rotation));\n" +
		"    float r_sin = sin(radians(rotation));\n" +
		"    mat2 rotationMatrix = mat2(r_cos, r_sin, -r_sin, r_cos);\n" +
		"    position = rotationMatrix * (scale * a_position) + translate;\n" +
		"  }\n" +
		"  else {\n" +
		"    position = scale * a_position + translate;\n" +
		"  }\n" +
		"  gl_Position = projection * vec4(position, 0.0, 1.0);\n" +
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
		// "#define LOWP lowp\n" +
		"precision mediump float;\n" +
		// "#else\n" +
		// "#define LOWP \n" +
		"#endif\n" +
		// "varying LOWP vec4 v_color;\n" +
		// "varying vec2 v_texCoords;\n" +
		// "uniform float v;\n" +
		// "uniform sampler2D u_texture;\n" +
		// // "uniform sampler2D u_texture2;\n" +
		// "void main()\n" +
		// "{\n" +
		// "  vec4 sum = vec4(0.0);\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 4.0 * v)) * 0.051;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 3.0 * v)) * 0.0918;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 2.0 * v)) * 0.12245;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y - 1.0 * v)) * 0.1531;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y)) * 0.1633;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 1.0 * v)) * 0.1531;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 2.0 * v)) * 0.12245;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 3.0 * v)) * 0.0918;\n" +
		// // "  sum += texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y + 4.0 * v)) * 0.051;\n" +
		// // "  gl_FragColor = v_color * mix(sum, texture2D(u_texture2, v_texCoords), 0.2);\n" +
		// // "  gl_FragColor = texture2D(u_texture, vec2(v_texCoords.x, v_texCoords.y));\n" +
		// "  gl_FragColor = sum;\n" +
		// "}";
		"uniform sampler2D u_texture;\n" +
		"uniform vec2 sampleOffset;\n" +
		"varying vec2 v_texCoords;\n" +
		"\n" +
		"float weights[21];\n" +
		"\n" +
		"void main()\n" +
		"{\n" +
		"	weights[0] = 0.009167927656011385;\n" +
		"	weights[1] = 0.014053461291849008;\n" +
		"	weights[2] = 0.020595286319257878;\n" +
		"	weights[3] = 0.028855245532226279;\n" +
		"	weights[4] = 0.038650411513543079;\n" +
		"	weights[5] = 0.049494378859311142;\n" +
		"	weights[6] = 0.060594058578763078;\n" +
		"	weights[7] = 0.070921288047096992;\n" +
		"	weights[8] = 0.079358891804948081;\n" +
		"	weights[9] = 0.084895951965930902;\n" +
		"	weights[10] = 0.086826196862124602;\n" +
		"	weights[11] = 0.084895951965930902;\n" +
		"	weights[12] = 0.079358891804948081;\n" +
		"	weights[13] = 0.070921288047096992;\n" +
		"	weights[14] = 0.060594058578763092;\n" +
		"	weights[15] = 0.049494378859311121;\n" +
		"	weights[16] = 0.038650411513543079;\n" +
		"	weights[17] = 0.028855245532226279;\n" +
		"	weights[18] = 0.020595286319257885;\n" +
		"	weights[19] = 0.014053461291849008;\n" +
		"	weights[20] = 0.009167927656011385;\n" +
		"\n" +
		"\n" +
		"	vec4 sum = vec4(0.0);\n" +
		"	vec2 offset = vec2(0.0);\n" +
		"	vec2 baseOffset = -10.0 * sampleOffset;\n" +
		"\n" +
		"	for (int s = 0; s < 21; ++s) {\n" +
		"		sum += texture2D(u_texture, v_texCoords.st + baseOffset + offset).rgba * weights[s];\n" +
		"		offset += sampleOffset;\n" +
		"	}\n" +
		"\n" +
		"	gl_FragColor = sum;\n" +
		"}\n";

	private ShaderProgram batchShader;

	private Counter scoreCounter;
	private Counter healthCounter;

	@Override
	public void create() {
		float width  = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		Gdx.graphics.setVSync(true);

		gl20 = Gdx.graphics.isGL20Available();
//		System.out.println("GL20: " + gl20);
//		System.out.println(vertexShader);
//		System.out.println(fragmentShader);
//		System.out.println("---------");
//		System.out.println(vertBlurVertexShader);
//		System.out.println(vertBlurFragmentShader);
//		System.out.println("---------");
//		System.out.println(batchVertexShader);
//		System.out.println(batchFragmentShader);
		if (gl20) {
			shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
			verticalBlurShader = new ShaderProgram(vertBlurVertexShader, vertBlurFragmentShader);
			batchShader = new ShaderProgram(batchVertexShader, batchFragmentShader);
		}
		spriteBatch = new SpriteBatch();

		if (gl20) {
			frameBuffer = new FrameBuffer(Format.RGBA4444, frameBufferSize, frameBufferSize, true);
			fboRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
			// fboRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			fboRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			fboRegion.flip(false, true);
	
			frameBuffer2 = new FrameBuffer(Format.RGBA4444, frameBufferSize, frameBufferSize, true);
			fboRegion2 = new TextureRegion(frameBuffer2.getColorBufferTexture());
			fboRegion2.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			fboRegion2.flip(false, true);
		}

		if (gl20) {
//			System.out.println("Compiled: " + shaderProgram.isCompiled() + "---------");
//			System.out.println("Compiled (vB): " + verticalBlurShader.isCompiled() + "---------");
//			System.out.println("Compiled (b): " + batchShader.isCompiled() + "---------");
		}

		new State(width, height, new Color(0.572f, 0.686f, 0.624f, 1.0f));

		String[] levelNames = {
			"level0.json",
			"level1.json"
		};

		loader = new LevelLoader(levelNames);
		player = new Player();
		level = loader.getLevel();

		State.setPlayer(player);
		State.setLoader(loader);
		State.setLevel(level);

		State.getStage().setShaderProgram(shaderProgram);
		StageTest simpleTest = new SimpleGameStageTest();
		StageTest shapesTest = new ShapesStageTest();
		StageTest textTest = new TextStageTest();

		simpleTest.load(State.getStage());

//		shapesTest.load(State.getStage());
//		textTest.load(State.getStage());

		float textWidth = 30;
		float textHeight = 30;
		float textLineWidth = 3.5f;
		Color textColor = new Color(0.941f, 0.941f, 0.827f, 0.75f);

		scoreCounter = new Counter(State.getWidth() * 0.5f, State.getHeight() * 0.95f, textColor, textWidth, textHeight, 10, textLineWidth);
		healthCounter = new Counter(State.getWidth() * 0.5f, State.getHeight() * 0.05f, textColor, textWidth, textHeight, 10, textLineWidth);
		State.getStage().addText(scoreCounter);
		State.getStage().addText(healthCounter);

		if (gl20) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		Gdx.input.setInputProcessor(new GameInputProcessor());
	}

	@Override
	public void dispose() {
		if (gl20) {
			shaderProgram.dispose();
			frameBuffer.dispose();
		}
	}

	@Override
	public void render() {
		if (!State.running)
			return;

		// Intersection testing code.
		if (State.debugRendering) {
			SnapshotArray<Actor> children = State.getStage().getTests().getChildren();
			Actor[] actors = children.begin();
			for (int i = 0, n = children.size; i < n; i++) {
				Actor child = actors[i];

				if (State.getStage().getEntities().hit(child.getX(), child.getY(), false) != null) {
					child.setColor(Color.GREEN);
				} else if (State.getStage().getText().hit(child.getX(), child.getY(), false) != null) {
					child.setColor(Color.BLUE);
				}
				else {
					child.setColor(Color.BLACK);
				}
			}

			children.end();
		}

		update();
		State.update();

		Color backgroundColor = State.getColor();

		if (State.debugRendering && gl20)
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if (!State.debugRendering && gl20) {
			Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			State.getStage().setShaderProgram(shaderProgram);
			State.getStage().draw();
		} else if (!State.debugRendering) {
			Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			State.getStage().draw();
		} else if (gl20) {
			// FrameBuffer1
			frameBuffer.begin();

			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			State.getStage().setShaderProgram(shaderProgram);
			State.getStage().draw();

			frameBuffer.end();

			//------ DON'T KNOW ABOUT THIS
			// state.getStage().setShaderProgram(verticalBlurShader);
			// fboRegion.getTexture().bind(0); // Binds it to texture 0.
			// Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
			// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			// verticalBlurShader.setUniformf("v", 1.0f / 512.0f);
			// verticalBlurShader.setUniformi("fboTexture", 0);
			// // Gdx.gl.glClearColor(0.5723f, 0.686f, 0.624f, 1.0f);
			// state.getStage().draw();

			//------ THIS WORKS
			frameBuffer2.begin();
			spriteBatch.begin();
			spriteBatch.setShader(batchShader);
			batchShader.setUniformf("sampleOffset", 1.0f / frameBufferSize, 0.0f);

			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			spriteBatch.draw(fboRegion, 0, 0, State.getWidth(), State.getHeight());

			spriteBatch.end();
			frameBuffer2.end();

			// Draw scene again.
			Gdx.gl.glClearColor(0.572f, 0.686f, 0.624f, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			State.getStage().setShaderProgram(shaderProgram);
			State.getStage().draw();

			// Overlay blur.
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			spriteBatch.begin();
			batchShader.setUniformf("sampleOffset", 0.0f, 1.0f / frameBufferSize);
			spriteBatch.draw(fboRegion2, 0, 0, State.getWidth(), State.getHeight());
			spriteBatch.end();
		}

		fpsLogger.log();
	}

	private void update() {
		scoreCounter.set(State.getPlayer().getScore());
		healthCounter.set(State.getPlayer().getHealth());

		// Lose screen.
		if (State.getPlayer().getHealth() <= 0 && !lost) {
			lost = true;

			TextMeshGroup lose = new TextMeshGroup("YOU LOSE", State.getWidth() * 0.5f, State.getHeight() * 0.8f, Color.WHITE, 60, 90, 30, 10.0f, Alignment.CENTER);
			lose.setAlignment(Alignment.CENTER);
			State.getStage().getText().addActor(lose);

			SnapshotArray<Actor> entityArray = State.getStage().getEntities().getChildren();
			Actor[] entityActors = entityArray.begin();
			for (int i = 0, n = entityArray.size; i < n; i++) {
				entityActors[i].clearActions();
				entityActors[i].addAction(fadeOut(0.8f, Interpolation.pow2Out));
			}
			entityArray.end();

			SnapshotArray<Actor> projectileArray = State.getStage().getProjectiles().getChildren();
			Actor[] projectileActors = projectileArray.begin();			
			for (int i = 0, n = projectileArray.size; i < n; i++) {
				projectileActors[i].clearActions();
				projectileActors[i].addAction(fadeOut(0.8f, Interpolation.pow2Out));
			}
			projectileArray.end();
			State.setLevel(null);
			
			State.getStage().addAction(
				sequence(
					color(Color.BLACK, 1.0f, Interpolation.pow2Out),
					new Action() {
						public boolean act(float delta) {
							State.running = false;
							sendScore(State.getPlayer().getScore());
							State.getStage().addText(new Scoreboard());
							return true;
						}
					}
				)				
			);
		}

		if (State.getStage().getRoot()!= null) {
			// State.getStage().getRoot().getChildren().get(0).setPosition(Gdx.input.getX(), -Gdx.input.getY() + State.getHeight());
			// if (((CircleMeshActor) State.getStage().getEntities().getChildren().get(0)).intersectsLine(0, 40, 1280, 40)) {
			// 	System.out.println("HELLO!");
			// }
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			System.out.println("entities: " + State.getStage().getEntities().getChildren().size);
			System.out.println("projectiles: " + State.getStage().getProjectiles().getChildren().size);
//			System.out.println("vel" + ((Bullet) State.getStage().getProjectiles().getChildren().get(0)).getVelocityX() + ", " + ((Bullet) State.getStage().getProjectiles().getChildren().get(0)).getVelocityY());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			if (!State.debugRendering) {
				State.debugRendering = true;
				System.out.println("Debug rendering on.");
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			if (State.debugRendering && gl20) {
				State.debugRendering = false;
				System.out.println("Debug rendering off.");
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			((MeshActor) State.getStage().getEntities().getChildren().get(0)).getEntity().takeFire();
			// System.out.println( camera.combined.toString() );
//			System.out.println( state.getCharacters().get(0).physics.getPosition().toString());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			State.running = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			SnapshotArray<Actor> children = State.getStage().getEntities().getChildren();
			Actor[] actors = children.begin();
			for (int i = 0, n = children.size; i < n; i++) {
				actors[i].rotate(1.0f);
			}

			children.end();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			SnapshotArray<Actor> children = State.getStage().getTests().getChildren();
			Actor[] actors = children.begin();
			for (int i = 0, n = children.size; i < n; i++) {
				actors[i].setY(actors[i].getY() - 2.0f);
			}

			children.end();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			State.getStage().clearActors();
		}
	}
	
	public void sendScore(int score) {
		String scoreString = "UPDATE PLAYER SET player_score = " + score + " WHERE PLAYER_ID = 1";
		try {
			Connection connection = Database.getConnection();
			//String query = "UPDATE TD_SCORE SET player_score = 4 WHERE PLAYER_ID = 1";
			
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(scoreString);

//	        ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYER_SCORE");
//	        while (rs.next()) {
//	            System.out.println(rs.getString(2) + ": " + rs.getString(3));
//	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
