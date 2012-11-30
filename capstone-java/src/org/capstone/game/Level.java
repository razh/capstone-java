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
	
	public Level(MeshStage stage) {
		setStage(stage);
	}
	
	public void act(float delta) {
		int numEnemyTypes = getNumEnemyTypes();
		for (int i = 0; i < numEnemyTypes; i++) {
			spawnTimes.set(i, spawnTimes.get(i) - delta);
		}
		
		for (int i = 0; i < numEnemyTypes; i++) {
			if (spawnTimes.get(i) < 0.0f) {
				if (spawnCounts.get(i) > 0) {
					stage.addEntity(entities.get(i));
					
					// Reset timer.
					spawnTimes.set(i, spawnIntervals.get(i));

					// Decrement spawn count.
					spawnCounts.set(i, spawnCounts.get(i) - 1);
				}
			}
		}
	}
	
	
	public int getNumEnemyTypes() {
		return entities.size();
	}	
	
	public MeshStage getStage() {
		return stage;
	}

	public void setStage(MeshStage stage) {
		this.stage = stage;
	}

	public void addEntitySpawner(Entity entity, float spawnTime, int spawnCount, float spawnInterval) {
		this.entities.add(entity);
		this.spawnTimes.add(spawnTime);
		this.spawnCounts.add(spawnCount);
		this.spawnIntervals.add(spawnInterval);
	}
}
