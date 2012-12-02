package org.capstone.game.entities;

import org.capstone.game.MeshActor;
import org.capstone.game.entities.weapons.Weapon;

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
			f.getDeclaringClass() == Weapon.class && (
				f.getName().equals("actor")   ||
				f.getName().equals("targetX") ||
				f.getName().equals("targetY")
			) ||
			f.getDeclaringClass() == LaserBeam.class && f.getName().equals("actor") ||
			f.getDeclaringClass() == MeshActor.class && f.getName().equals("entity")
		);
	}

}
