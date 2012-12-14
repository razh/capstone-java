package org.capstone.game.json;

import org.capstone.game.entities.Entity;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

// When serializing Actor first.
public class ActorExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if (f.getDeclaringClass() == Entity.class && (
			f.getName().equals("actor")
		)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
