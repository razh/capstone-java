package org.capstone.game.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.google.gson.ExclusionStrategy;
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
		long start = System.nanoTime();
		JsonObject object = new JsonObject();

		if (src instanceof MoveToAction) {
			MoveToAction action = (MoveToAction) src;
			object.add("x", context.serialize(action.getX()));
			object.add("y", context.serialize(action.getY()));
		} else if (src instanceof MoveByAction) {
			MoveByAction action = (MoveByAction) src;
			object.add("amountX", context.serialize(action.getAmountX()));
			object.add("amountY", context.serialize(action.getAmountY()));
		} else if (src instanceof SizeToAction) {
			SizeToAction action = (SizeToAction) src;
			object.add("width", context.serialize(action.getWidth()));
			object.add("height", context.serialize(action.getHeight()));
		} else if (src instanceof SizeByAction) {
			SizeByAction action = (SizeByAction) src;
			object.add("amountWidth", context.serialize(action.getAmountWidth()));
			object.add("amountHeight", context.serialize(action.getAmountHeight()));
		}
		
		if (src instanceof ParallelAction) {
			ParallelAction action = (ParallelAction) src;
			object.add("actions", context.serialize(action.getActions()));
		}
		
		if (src instanceof TemporalAction) {
			TemporalAction action = (TemporalAction) src;
			object.add("duration", context.serialize(action.getDuration()));
			object.add("interpolation", context.serialize(action.getInterpolation()));
		}

		System.out.println("-+-+-+" + (System.nanoTime() - start));
		return object;
	}

	@Override
	public Action deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
