package org.capstone.game;

import org.capstone.game.entities.CircleEntity;
import org.capstone.game.entities.Entity;
import org.capstone.game.entities.EntityGroup;
import org.capstone.game.entities.PolygonEntity;
import org.capstone.game.entities.RectEntity;
import org.capstone.game.entities.weapons.BulletGun;
import org.capstone.game.entities.weapons.LaserGun;
import org.capstone.game.entities.weapons.Weapon;
import org.capstone.game.json.ActionAdapter;
import org.capstone.game.json.ArraySerializer;
import org.capstone.game.json.InterpolationAdapter;
import org.capstone.game.json.MeshActorDeserializer;
import org.capstone.game.json.GlobalExclusionStrategy;
import org.capstone.game.json.MeshGroupSerializer;
import org.capstone.game.json.SnapshotArraySerializer;
import org.capstone.game.json.WeaponAdapter;
import org.capstone.game.ui.ScoreCounter;

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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Game implements ApplicationListener {
	private FrameBuffer frameBuffer;
	private FrameBuffer frameBuffer2;
	private SpriteBatch spriteBatch;
	private TextureRegion fboRegion;
	private TextureRegion fboRegion2;
	private int frameBufferSize = 512;
	private FPSLogger fpsLogger = new FPSLogger();
	private boolean running = true;
	private boolean gl20 = false;

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
		"    position = rotationMatrix * (a_position * scale) + translate;\n" +
		"  }\n" +
		"  else {\n" +
		"    position = a_position * scale + translate;\n" +
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
	
	private ScoreCounter scoreCounter;
	private TextMeshGroup quickfox;

	@Override
	public void create() {
		float width  = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		Gdx.graphics.setVSync(true);

		gl20 = Gdx.graphics.isGL20Available();
		System.out.println("GL20: " + gl20);
		System.out.println(vertexShader);
		System.out.println(fragmentShader);
		System.out.println("---------");
		System.out.println(vertBlurVertexShader);
		System.out.println(vertBlurFragmentShader);
		System.out.println("---------");
		System.out.println(batchVertexShader);
		System.out.println(batchFragmentShader);
		shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		verticalBlurShader = new ShaderProgram(vertBlurVertexShader, vertBlurFragmentShader);
		batchShader = new ShaderProgram(batchVertexShader, batchFragmentShader);
		spriteBatch = new SpriteBatch();

		frameBuffer = new FrameBuffer(Format.RGBA4444, frameBufferSize, frameBufferSize, true);
		fboRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		// fboRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fboRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fboRegion.flip(false, true);

		frameBuffer2 = new FrameBuffer(Format.RGBA4444, frameBufferSize, frameBufferSize, true);
		fboRegion2 = new TextureRegion(frameBuffer2.getColorBufferTexture());
		fboRegion2.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fboRegion2.flip(false, true);

		System.out.println("Compiled: " + shaderProgram.isCompiled() + "---------");
		System.out.println("Compiled (vB): " + verticalBlurShader.isCompiled() + "---------");
		System.out.println("Compiled (b): " + batchShader.isCompiled() + "---------");

		Entity redCircle = new CircleEntity(100, 200, new Color(0.941f, 0.247f, 0.208f, 1.0f), 30);
		redCircle.addWeapon(new BulletGun(redCircle, 1.0f, 0.15f, -1.0f, 600.0f, new Color(0.106f, 0.126f, 0.146f, 1.0f), 4.0f));
		redCircle.addWeapon(new LaserGun(redCircle, 1.0f, 0.2f, 200.0f, new Color(0.941f, 0.404f, 0.365f, 0.75f), 1.5f));
		redCircle.setVelocity(200.0f, 100.0f);
		redCircle.setTeam(1);
//		redCircle.setRotation(25);

		new State(width, height, new Color(0.572f, 0.686f, 0.624f, 1.0f));
		player = new Player();
		level = new Level();
		level.addEntitySpawner(redCircle, 1.5f, 100, 1.5f);
		State.setLevel(level);
		State.setPlayer(player);

		Entity blueCircle = new CircleEntity(200, 200, new Color(0.173f, 0.204f, 0.220f, 1.0f), 30);
		blueCircle.setVelocity(100.0f, 100.0f);

		Entity whiteRect = new RectEntity(300, 400, new Color(0.941f, 0.941f, 0.827f, 0.5f), 30, 70);

		Entity redRect = new RectEntity(200, 40, new Color(0.941f, 0.247f, 0.208f, 1.0f), 100.0f, 20.0f);
		redRect.setTeam(1);
		redRect.setRotation(35.0f);
		redRect.setOriented(true);
		redRect.getActor().setTouchable(Touchable.disabled);
		redRect.setVelocity(-50.0f, 100.0f);

		Entity group = new EntityGroup(MeshType.RectMeshActor, 400, 400, new Color(0.941f, 0.247f, 0.208f, 1.0f), 20, 10, 10, 60);
		group.setVelocity(200.0f, 100.0f);
		((EntityGroup) group).setOriented(true);

		Entity group2 = new EntityGroup(MeshType.CircleMeshActor, 600, 400, new Color(0.173f, 0.204f, 0.220f, 1.0f), 20, 20, 10, 80);
		group2.setVelocity(-200.0f, 100.0f);

		State.getStage().setShaderProgram(shaderProgram);

		State.getStage().addEntity(redCircle);
		State.getStage().addEntity(blueCircle);
		State.getStage().addEntity(whiteRect);
		State.getStage().addEntity(group);
		State.getStage().addEntity(group2);
		State.getStage().addEntity(redRect);
		float textWidth = 30;
		float textHeight = 30;
		float textLineWidth = 3.5f;
		Color textColor = new Color(0.941f, 0.941f, 0.827f, 0.75f);
		State.getStage().addText(new TextMeshActor('0',  40, 100, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('1',  80, 100, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('2',  40, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('3',  80, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('4', 120, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('5', 160, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('6', 200, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('7', 240, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('8', 280, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('9', 320, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('A', 360, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('B', 400, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('C', 440, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('D', 480, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('E', 520, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('F', 560, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('G', 600, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('H', 640, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('I', 680, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('J', 720, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('K', 760, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('L', 800, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('M', 840, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('N', 880, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('O', 920, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('P', 960, 200, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('Q',  40, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('R',  80, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('S', 120, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('T', 160, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('U', 200, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('V', 240, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('W', 280, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('X', 320, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('Y', 360, 300, textColor, textWidth, textHeight, textLineWidth));
		State.getStage().addText(new TextMeshActor('Z', 400, 300, textColor, textWidth, textHeight, textLineWidth));
		quickfox = new TextMeshGroup("THE QUICK BROWN FOX", 200, 100, new Color(0.2f, 0.4f, 0.3f, 1.0f), 30, 50, 10, 4.0f);
		quickfox.setName("FOX");
		State.getStage().addText(quickfox);
		
		scoreCounter = new ScoreCounter(640, 750, textColor, textWidth, textHeight, 10, textLineWidth);
		State.getStage().addText(scoreCounter);

		float[] testVertices = new float[8 * 2];
		float subdivAngle = (float) (Math.PI * 2 / 8);
		int vtxIndex = 0;
		for (int i = 0; i < 8; i++) {
			testVertices[vtxIndex++] = (float) Math.sin(i * subdivAngle);
			testVertices[vtxIndex++] = (float) Math.cos(i * subdivAngle);
		}
		// Clockwise order.
		State.getStage().addEntity(new PolygonEntity(new float[] {-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 0.0f}, 800, 500, new Color(0.149f, 0.266f, 0.380f, 0.5f), 50, 60));

		// State.getStage().addEntity(new PolygonEntity(new float[] {-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 0.0f}, 800, 500, new Color(0.0f, 0.5f, 0.0f, 0.5f), 50, 60));
		// Counterclockwise order.
		State.getStage().addEntity(new PolygonEntity(new float[] {-1.0f, 0.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f}, 300, 600, new Color(0.420f, 0.384f, 0.388f, 0.5f), 50, 60));
		State.getStage().addEntity(new PolygonEntity(testVertices, 200, 500, new Color(0.0f, 0.25f, 0.0f, 1.0f), 20, 30));

		// Test intersection grid.
		RectEntity rectEnt;
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				rectEnt = new RectEntity(350 + i * 4, 430 + j * 4, new Color(Color.BLACK), 1, 1);
				State.getStage().addTest(rectEnt);
			}
		}

//		for (int i = 0; i < 500; i++) {
//			Character ctest = new Character(i, i, new Color(i / 500.0f, i / 10000.0f, 0.24f, 1.0f), 10);
//			ctest.setVelocity((float) Math.random() * 200.0f, (float) Math.random() * 200.0f);
//			State.getStage().addCharacter(ctest);
//		}

		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new GlobalExclusionStrategy())
//			.setExclusionStrategies(new ActionExclusionStrategy())
			.registerTypeHierarchyAdapter(Action.class, new ActionAdapter())
			.registerTypeHierarchyAdapter(Weapon.class, new WeaponAdapter())
			.registerTypeHierarchyAdapter(Interpolation.class, new InterpolationAdapter())
			.registerTypeHierarchyAdapter(Array.class, new ArraySerializer())
			.registerTypeAdapter(SnapshotArray.class, new SnapshotArraySerializer())
			.registerTypeHierarchyAdapter(MeshActor.class, new MeshActorDeserializer())
			.registerTypeAdapter(MeshGroup.class, new MeshGroupSerializer())
			.serializeNulls()
			.create();

		redCircle.addAction(
			sequence(
				delay(2.0f),
				sizeBy(50.0f, 50.0f, 0.5f, Interpolation.elastic),
				delay(0.2f),
				sizeBy(50.0f, 50.0f, 1.0f, Interpolation.elastic),
				delay(1.0f),
				parallel(
					color(new Color(0.5f, 0.2f, 0.3f, 1.0f), 2.0f, Interpolation.circle),
					sizeBy(-100.0f, -100.0f, 2.0f, Interpolation.bounceOut),
					rotateTo(20.0f)
				)
			)
		);
		
		Entity movingThingy = new CircleEntity(200, 300, new Color(0.0f, 1.0f, 0.0f, 1.0f), 30.0f);
		movingThingy.addAction(
			forever(
				sequence(
						moveBy(200, 200, 1.0f, Interpolation.exp10Out),
						moveBy(-200, 200, 1.0f, Interpolation.exp10Out),
						moveBy(-200, -200, 1.0f, Interpolation.exp10),
						moveBy(200, -200, 1.0f, Interpolation.exp5Out)
				)
			)
		);
		State.getStage().addEntity(movingThingy);

		String json;
		System.out.println("GROUP2-----");
		json = gson.toJson(group2.getActor());
		System.out.println(json);
		System.out.println("REDCIRCLE-----");
		json = gson.toJson(redCircle.getActor());
		MeshActor testActor = gson.fromJson(json, MeshActor.class);
		System.out.println(json);
		System.out.println("ENTITIES-----");
		json = gson.toJson(State.getStage().getEntities());
		System.out.println(json);
		System.out.println("STAGE-----");
		json = gson.toJson(State.getStage());
		System.out.println(json);
		System.out.println("REJSON-----");
		json = gson.toJson(testActor);
		System.out.println(json);
		System.out.println("LEVEL-----");
		json = gson.toJson(level);
		System.out.println(json);
		System.out.println("DESERLEVEL-----");
		Level deserializedLevel = gson.fromJson(json, Level.class);
		json = gson.toJson(deserializedLevel);
		System.out.println(json);
		System.out.println("PLAYER-----");
		json = gson.toJson(State.getPlayer());
		System.out.println(json);
		System.out.println("MOVINGTHINGY-----");
		json = gson.toJson(movingThingy.getActor());
		System.out.println(json);
		
		State.getStage().addAction(color(new Color(0.5f, 0.5f, 0.5f, 1.0f), 10.0f, Interpolation.pow3));

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
		if (!running)
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

		handleInput();
		State.update();

		Color backgroundColor = State.getColor();

		if (State.debugRendering)
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if (!State.debugRendering) {
			Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			State.getStage().setShaderProgram(shaderProgram);
			State.getStage().draw();
		} else {
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

	private void handleInput() {
		State.getPlayer().addScore(1);
		scoreCounter.setScore(State.getPlayer().getScore());
//		quickfox.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		
//		State.getStage().getCharacters().getChildren().get(0).rotate(1.0f);
//		MeshGroup test = State.getStage().getProjectiles();
//		if (test != null)
//			System.out.println("size" + test.getChildren().size);
//
		if (State.getStage().getRoot()!= null) {
			// State.getStage().getRoot().getChildren().get(0).setPosition(Gdx.input.getX(), -Gdx.input.getY() + State.getHeight());
			// if (((CircleMeshActor) State.getStage().getEntities().getChildren().get(0)).intersectsLine(0, 40, 1280, 40)) {
			// 	System.out.println("HELLO!");
			// }
		}

		if (Gdx.input.isTouched()) {
			if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				Actor hit = State.getStage().getEntities().hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true);
				if (hit != null) {
					hit.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
					((PhysicsActor) hit).setVelocity(0.0f, 0.0f);
				}
				else {
					hit = State.getStage().getText().hit(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), true);
					if (hit != null) {
						hit.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						if (hit instanceof PhysicsActor)
							((PhysicsActor) hit).setVelocity(0.0f, 0.0f);
						else if (hit instanceof MeshGroup) {
							((MeshGroup) hit).setVelocity(0.0f, 0.0f);
						}
					}
				}
			} else {
				State.getStage().getEntities().getChildren().get(0).setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}
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
			if (State.debugRendering) {
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
			running = false;
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

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
