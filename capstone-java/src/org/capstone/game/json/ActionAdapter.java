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
		JsonObject object = new JsonObject();

		if (src instanceof MoveToAction) {
			object = addSerializedMoveToAction(object, (MoveToAction) src, context);
		} else if (src instanceof MoveByAction) {
			object = addSerializedMoveByAction(object, (MoveByAction) src, context);
		} else if (src instanceof SizeToAction) {
			object = addSerializedSizeToAction(object, (SizeToAction) src, context);
		} else if (src instanceof SizeByAction) {
			object = addSerializedSizeByAction(object, (SizeByAction) src, context);
		} else if (src instanceof RotateToAction) {
			object = addSerializedRotateToAction(object, (RotateToAction) src, context);
		} else if (src instanceof RotateByAction) {
			object = addSerializedRotateByAction(object, (RotateByAction) src, context);
		}

		if (src instanceof DelegateAction) {
			if (src instanceof DelayAction) {
				DelayAction action = (DelayAction) src;
				object.add("duration", context.serialize(action.getDuration()));
			}
			object = addSerializedDelegateAction(object, (DelegateAction) src, context);
		}

		if (src instanceof ParallelAction) {
			if (src instanceof SequenceAction) {
				object.add("type", context.serialize("sequence"));
			} else {
				object.add("type", context.serialize("parallel"));
			}
			ParallelAction action = (ParallelAction) src;
			object.add("actions", context.serialize(action.getActions()));
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

	private JsonObject addSerializedMoveToAction(JsonObject object, MoveToAction action, JsonSerializationContext context) {
		object.add("x", context.serialize(action.getX()));
		object.add("y", context.serialize(action.getY()));
		return object;
	}

	private JsonObject addSerializedMoveByAction(JsonObject object, MoveByAction action, JsonSerializationContext context) {
		object.add("amountX", context.serialize(action.getAmountX()));
		object.add("amountY", context.serialize(action.getAmountY()));
		return object;
	}

	private JsonObject addSerializedSizeToAction(JsonObject object, SizeToAction action, JsonSerializationContext context) {
		object.add("width", context.serialize(action.getWidth()));
		object.add("height", context.serialize(action.getHeight()));
		return object;
	}

	private JsonObject addSerializedSizeByAction(JsonObject object, SizeByAction action, JsonSerializationContext context) {
		object.add("amountWidth", context.serialize(action.getAmountWidth()));
		object.add("amountHeight", context.serialize(action.getAmountHeight()));
		return object;
	}

	private JsonObject addSerializedRotateToAction(JsonObject object, RotateToAction action, JsonSerializationContext context) {
		object.add("angle", context.serialize(action.getRotation()));
		return object;
	}

	private JsonObject addSerializedRotateByAction(JsonObject object, RotateByAction action, JsonSerializationContext context) {
		object.add("rotation", context.serialize(action.getAmount()));
		return object;
	}

	private JsonObject addSerializedDelegateAction(JsonObject object, DelegateAction action, JsonSerializationContext context) {
		Action delegatedAction = action.getAction();
		if (delegatedAction != null)
			object.add("action",context.serialize(delegatedAction));

		return object;
	}
}
