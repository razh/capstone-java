package org.capstone.game;

import java.util.ArrayList;

import org.capstone.game.entities.Entity;

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
	
	private boolean complete;

	public Level() {
		entities = new ArrayList<Entity>();
		spawnTimes = new ArrayList<Float>();
		spawnCounts = new ArrayList<Integer>();
		spawnIntervals = new ArrayList<Float>();
		setComplete(false);
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

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public MeshStage getStage() {
		return stage;
	}

	public void setStage(MeshStage stage) {
		this.stage = stage;
	}

	public void addEntitySpawner(Entity entity, float spawnTime, int spawnCount, float spawnInterval) {
		this.entities.add(new Entity(entity));
		this.spawnTimes.add(spawnTime);
		this.spawnCounts.add(spawnCount);
		this.spawnIntervals.add(spawnInterval);
	}
}
