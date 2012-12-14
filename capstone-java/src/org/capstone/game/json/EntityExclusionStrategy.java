package org.capstone.game.json;

import org.capstone.game.MeshActor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

// When serializing Entity first.
public class EntityExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if ((f.getDeclaringClass() == Actor.class && (
			f.getName().equals("entity")
		)) ||
		(f.getDeclaringClass() == MeshActor.class && (
			f.getName().equals("entity")
		))) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
