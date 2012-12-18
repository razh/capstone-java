package org.capstone.game.tests;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeBy;

import org.capstone.game.Level;
import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.MeshStage;
import org.capstone.game.MeshType;
import org.capstone.game.State;
import org.capstone.game.entities.CircleEntity;
import org.capstone.game.entities.Entity;
import org.capstone.game.entities.EntityGroup;
import org.capstone.game.entities.PolygonEntity;
import org.capstone.game.entities.RectEntity;
import org.capstone.game.entities.weapons.BulletGun;
import org.capstone.game.entities.weapons.LaserGun;
import org.capstone.game.entities.weapons.Weapon;
import org.capstone.game.json.ActionAdapter;
import org.capstone.game.json.ActorExclusionStrategy;
import org.capstone.game.json.ArraySerializer;
import org.capstone.game.json.EntityDeserializer;
import org.capstone.game.json.EntityExclusionStrategy;
import org.capstone.game.json.GlobalExclusionStrategy;
import org.capstone.game.json.InterpolationAdapter;
import org.capstone.game.json.MeshActorDeserializer;
import org.capstone.game.json.MeshGroupSerializer;
import org.capstone.game.json.SnapshotArraySerializer;
import org.capstone.game.json.WeaponAdapter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ShapesStageTest extends StageTest {

	@Override
	public void load(MeshStage stage) {
		Entity redCircle = new CircleEntity(100, 200, new Color(0.941f, 0.247f, 0.208f, 1.0f), 30);
		redCircle.addWeapon(new BulletGun(redCircle, 1.0f, 0.15f, -1.0f, 600.0f, new Color(0.106f, 0.126f, 0.146f, 1.0f), 4.0f));
		redCircle.getWeapons().get(0).setRange(200.0f);
		((BulletGun) redCircle.getWeapons().get(0)).setBulletRange(200.0f);
		redCircle.addWeapon(new LaserGun(redCircle, 1.0f, 0.2f, 200.0f, new Color(0.941f, 0.404f, 0.365f, 0.75f), 1.5f));
		redCircle.setVelocity(200.0f, 100.0f);
		redCircle.setTeam(1);

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

		stage.addEntity(redCircle);
		stage.addEntity(blueCircle);
		stage.addEntity(whiteRect);
		stage.addEntity(group);
		stage.addEntity(group2);
		stage.addEntity(redRect);

		float[] testVertices = new float[8 * 2];
		float subdivAngle = (float) (Math.PI * 2 / 8);
		int vtxIndex = 0;
		for (int i = 0; i < 8; i++) {
			testVertices[vtxIndex++] = (float) Math.sin(i * subdivAngle);
			testVertices[vtxIndex++] = (float) Math.cos(i * subdivAngle);
		}

		// Clockwise order.
		stage.addEntity(new PolygonEntity(new float[] {-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 0.0f}, 800, 500, new Color(0.149f, 0.266f, 0.380f, 0.5f), 50, 60));
		Entity diamond = new PolygonEntity(new float[] {0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f}, 900, 600, new Color(0.149f, 0.266f, 0.380f, 0.5f), 50, 50);
		diamond.addAction(
			forever(
				parallel(
					rotateBy(360, 2.0f),
					sequence(
						moveBy(200, 200, 0.5f, Interpolation.exp10Out),
						moveBy(-200, 200, 0.5f, Interpolation.exp10Out),
						moveBy(-200, -200, 0.5f, Interpolation.exp10),
						moveBy(200, -200, 0.5f, Interpolation.exp5Out)
					)
				)
			)
		);
		diamond.setLifeTime(2.0f);
		diamond.getActor().setTouchable(Touchable.disabled);
//		level.addEntitySpawner(diamond, 3.0f, 100, 1.5f);

		Entity triangle = new PolygonEntity(new float[] {-1.0f, -0.732f, 1.0f, -0.732f, 0.0f, 1.0f}, 1000, 700, new Color(0.149f, 0.266f, 0.380f, 0.5f), 50, 50);

		stage.addEntity(diamond);
		stage.addEntity(triangle);

		// Counterclockwise order.
		stage.addEntity(new PolygonEntity(new float[] {-1.0f, 0.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f}, 300, 600, new Color(0.420f, 0.384f, 0.388f, 0.5f), 50, 60));
		stage.addEntity(new PolygonEntity(testVertices, 200, 500, new Color(0.0f, 0.25f, 0.0f, 1.0f), 20, 30));

		// Test intersection grid.
		RectEntity rectEnt;
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				rectEnt = new RectEntity(350 + i * 4, 430 + j * 4, new Color(Color.BLACK), 1, 1);
				stage.addTest(rectEnt);
			}
		}

//		for (int i = 0; i < 500; i++) {
//			Character ctest = new Character(i, i, new Color(i / 500.0f, i / 10000.0f, 0.24f, 1.0f), 10);
//			ctest.setVelocity((float) Math.random() * 200.0f, (float) Math.random() * 200.0f);
//			stage.addCharacter(ctest);
//		}

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
		stage.addEntity(movingThingy);

		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new ActorExclusionStrategy())
			.setExclusionStrategies(new GlobalExclusionStrategy())
			.registerTypeHierarchyAdapter(Action.class, new ActionAdapter())
			.registerTypeHierarchyAdapter(Weapon.class, new WeaponAdapter())
			.registerTypeHierarchyAdapter(Interpolation.class, new InterpolationAdapter())
			.registerTypeHierarchyAdapter(Array.class, new ArraySerializer())
			.registerTypeAdapter(SnapshotArray.class, new SnapshotArraySerializer())
			.registerTypeHierarchyAdapter(MeshActor.class, new MeshActorDeserializer())
			.registerTypeHierarchyAdapter(Entity.class, new EntityDeserializer())
			.registerTypeAdapter(MeshGroup.class, new MeshGroupSerializer())
			.serializeNulls()
			.create();

		Gson levelGson = new GsonBuilder()
			.setExclusionStrategies(new EntityExclusionStrategy())
			.setExclusionStrategies(new GlobalExclusionStrategy())
			.registerTypeHierarchyAdapter(Action.class, new ActionAdapter())
			.registerTypeHierarchyAdapter(Weapon.class, new WeaponAdapter())
			.registerTypeHierarchyAdapter(Interpolation.class, new InterpolationAdapter())
			.registerTypeHierarchyAdapter(Array.class, new ArraySerializer())
			.registerTypeAdapter(SnapshotArray.class, new SnapshotArraySerializer())
			.registerTypeHierarchyAdapter(MeshActor.class, new MeshActorDeserializer())
			.registerTypeHierarchyAdapter(Entity.class, new EntityDeserializer())
			.registerTypeAdapter(MeshGroup.class, new MeshGroupSerializer())
			.serializeNulls()
			.create();

//		String json;
//		System.out.println("GROUP2-----");
//		json = gson.toJson(group2.getActor());
//		System.out.println(json);
//		System.out.println("REDCIRCLE-----");
//		json = gson.toJson(redCircle.getActor());
//		MeshActor testActor = gson.fromJson(json, MeshActor.class);
//		System.out.println(json);
//		System.out.println("ENTITIES-----");
//		json = gson.toJson(stage.getEntities());
//		System.out.println(json);
//		System.out.println("STAGE-----");
//		json = gson.toJson(stage);
//		System.out.println(json);
//		System.out.println("REJSON-----");
//		json = gson.toJson(testActor);
//		System.out.println(json);
//		System.out.println("LEVEL-----");
//		json = levelGson.toJson(State.getLevel());
//		System.out.println(json);
//		System.out.println("DESERLEVEL-----");
//		Level deserializedLevel = gson.fromJson(json, Level.class);
//		json = levelGson.toJson(deserializedLevel);
//		System.out.println(json);
//		System.out.println("PLAYER-----");
//		json = gson.toJson(State.getPlayer());
//		System.out.println(json);
//		System.out.println("MOVINGTHINGY-----");
//		json = gson.toJson(movingThingy.getActor());
//		System.out.println(json);
	}

}
