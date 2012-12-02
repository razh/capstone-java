package org.capstone.game.entities;

import org.capstone.game.MeshActor;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class EntityExclusionStrategy implements ExclusionStrategy {

	public EntityExclusionStrategy() {
	}

	@Override
	public boolean shouldSkipClass(Class<?> c) {
		return false;
//		return (c == Actor.class);
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return (
			f.getDeclaringClass() == Entity.class && f.getName().equals("weapons") ||
			f.getDeclaringClass() == MeshActor.class && f.getName().equals("entity")
		);
	}
	
}
