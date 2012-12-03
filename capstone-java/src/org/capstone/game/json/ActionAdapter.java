package org.capstone.game.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.badlogic.gdx.scenes.scene2d.actions.*;

public class ActionAdapter implements JsonSerializer<Action>, JsonDeserializer<Action> {
	@Override
	public JsonElement serialize(Action src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject object = new JsonObject();

		if (src instanceof MoveToAction) {
			MoveToAction action = (MoveToAction) src;
			object.add("x", context.serialize(action.getX()));
			object.add("y", context.serialize(action.getY()));
		}
		
		if (src instanceof TemporalAction) {
			TemporalAction action = (TemporalAction) src;
			object.add("duration", context.serialize(action.getDuration()));
			object.add("interpolation", context.serialize(action.getInterpolation()));
		}

		return object;
	}

	@Override
	public Action deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
