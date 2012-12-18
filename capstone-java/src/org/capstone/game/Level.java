package org.capstone.game;

import java.util.ArrayList;

import org.capstone.game.entities.Entity;

import com.badlogic.gdx.scenes.scene2d.Action;

public class Level {
	// All the prototypical entities to be spawned.
	private ArrayList<Entity> entities;

	// Initial spawning time.
	private ArrayList<Float> spawnTimes;

	// Number of times to spawn.
	private ArrayList<Integer> spawnCounts;

	// Time interval between spawning.
	private ArrayList<Float> spawnIntervals;

	private MeshStage stage;

	private float startTime;
	private boolean complete;
	private ArrayList<Action> actions;

	public Level() {
		entities = new ArrayList<Entity>();
		spawnTimes = new ArrayList<Float>();
		spawnCounts = new ArrayList<Integer>();
		spawnIntervals = new ArrayList<Float>();

		setStartTime(0.0f);
		setComplete(false);
		actions = new ArrayList<Action>();
	}

	public Level(MeshStage stage) {
		this();
		setStage(stage);
	}

	public void act(float delta) {
		if (isComplete())
			return;

		int numEnemyTypes = getNumEnemyTypes();
		for (int i = 0; i < numEnemyTypes; i++) {
			spawnTimes.set(i, spawnTimes.get(i) - delta);
		}

		Entity entity;
		int numComplete = 0;
		for (int i = 0; i < numEnemyTypes; i++) {
			if (spawnTimes.get(i) < 0.0f) {
				if (spawnCounts.get(i) > 0) {
					if (stage != null) {
						entity = new Entity(entities.get(i));
						stage.addEntity(entity);
					}

					// Reset timer.
					spawnTimes.set(i, spawnIntervals.get(i));

					// Decrement spawn count.
					spawnCounts.set(i, spawnCounts.get(i) - 1);
				} else {
					numComplete++;
				}
			}
		}

		if (numComplete == numEnemyTypes) {
			setComplete(true);
		}
	}


	public int getNumEnemyTypes() {
		return entities.size();
	}

	public float getStartTime() {
		return startTime;
	}

	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public void addAction(Action action) {
		actions.add(action);

		stage.addAction(action);
	}

	public MeshStage getStage() {
		return stage;
	}

	public void setStage(MeshStage stage) {
		this.stage = stage;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public ArrayList<Float> getSpawnTimes() {
		return spawnTimes;
	}

	public ArrayList<Integer> getSpawnCounts() {
		return spawnCounts;
	}
	public ArrayList<Float> getSpawnIntervals() {
		return spawnIntervals;
	}

	public void addEntitySpawner(Entity entity, float spawnTime, int spawnCount, float spawnInterval) {
		entities.add(new Entity(entity));
		spawnTimes.add(spawnTime);
		spawnCounts.add(spawnCount);
		spawnIntervals.add(spawnInterval);
		
		setComplete(false);
	}
}
