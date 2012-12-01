package org.capstone.game;

import java.util.ArrayList;

import org.capstone.game.entities.Entity;

public class Level {
	// All the prototypical entities to be spawned.
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	// Initial spawning time.
	private ArrayList<Float> spawnTimes = new ArrayList<Float>();

	// Number of times to spawn.
	private ArrayList<Integer> spawnCounts = new ArrayList<Integer>();

	// Time interval between spawning.
	private ArrayList<Float> spawnIntervals = new ArrayList<Float>();

	private MeshStage stage;

	private ArrayList<Entity> addedEntities = new ArrayList<Entity>();

	public Level() {}

	public Level(MeshStage stage) {
		setStage(stage);
	}

	public void act(float delta) {
		int numEnemyTypes = getNumEnemyTypes();
		for (int i = 0; i < numEnemyTypes; i++) {
			spawnTimes.set(i, spawnTimes.get(i) - delta);
		}

		Entity entity;
		for (int i = 0; i < numEnemyTypes; i++) {
			if (spawnTimes.get(i) < 0.0f) {
				if (spawnCounts.get(i) > 0) {
					if (stage != null) {
						entity = new Entity(entities.get(i));
						stage.addEntity(entity);
						addedEntities.add(entity);
						System.out.println("Adding entity.");
					}

					// Reset timer.
					spawnTimes.set(i, spawnIntervals.get(i));

					// Decrement spawn count.
					spawnCounts.set(i, spawnCounts.get(i) - 1);
				}
			}
		}

		for (int i = 0; i < addedEntities.size(); i++) {
//			System.out.println(i + ": " + addedEntities.get(i).getX() + ", " + addedEntities.get(i).getY());
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
		this.entities.add(new Entity(entity));
		this.spawnTimes.add(spawnTime);
		this.spawnCounts.add(spawnCount);
		this.spawnIntervals.add(spawnInterval);
	}
}
