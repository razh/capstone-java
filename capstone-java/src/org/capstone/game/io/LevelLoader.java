package org.capstone.game.io;

import org.capstone.game.Level;
import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.State;
import org.capstone.game.entities.Entity;
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LevelLoader {
	private Gson gson;
	private String[] levelNames = {
		"testLevel.json"
	};
	private Level level;

	public LevelLoader(Gson gson) {
		setGson(gson);
		level = new Level();
		State.setLevel(level);
		
		loadLevels();
	}
	
	public LevelLoader() {
		this(
			new GsonBuilder()
				.registerTypeHierarchyAdapter(Action.class, new ActionAdapter())
				.registerTypeHierarchyAdapter(Weapon.class, new WeaponAdapter())
				.registerTypeHierarchyAdapter(Interpolation.class, new InterpolationAdapter())
				.registerTypeHierarchyAdapter(Array.class, new ArraySerializer())
				.registerTypeAdapter(SnapshotArray.class, new SnapshotArraySerializer())
				.registerTypeHierarchyAdapter(MeshActor.class, new MeshActorDeserializer())
				.registerTypeHierarchyAdapter(Entity.class, new EntityDeserializer())
				.registerTypeAdapter(MeshGroup.class, new MeshGroupSerializer())
				.serializeNulls()
				.create()
		);
	}

	private void loadLevels() {
		try {
			FileHandle file;
			String json;
			Level tempLevel;
			for (int i = 0; i < levelNames.length; i++) {
				file = Gdx.files.internal(levelNames[i]);

				json = file.readString();
				if (json == null || json.isEmpty())
					return;

				if (gson != null) {
					tempLevel = gson.fromJson(json, Level.class);
				} else {
					tempLevel = null;
				}
				
				if (tempLevel != null) {
					int numEnemyTypes = tempLevel.getNumEnemyTypes();
					for (int j = 0; j < numEnemyTypes; j++) {
						level.addEntitySpawner(tempLevel.getEntities().get(i),
						                       tempLevel.getSpawnTimes().get(i),
						                       tempLevel.getSpawnCounts().get(i),
						                       tempLevel.getSpawnIntervals().get(i));
						System.out.println("ADDING");
					}
				}
			}
		} catch (GdxRuntimeException e) {
			e.printStackTrace();
		}
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void act(float delta) {

	}

}
