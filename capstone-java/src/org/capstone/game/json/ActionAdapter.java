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

		// Temporal actions.
		if (src instanceof TemporalAction) {
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
			} else if (src instanceof ColorAction) {
				object = addSerializedColorAction(object, (ColorAction) src, context);
			} else if (src instanceof AlphaAction) {
				object = addSerializedAlphaAction(object, (AlphaAction) src, context);
			}
			object = addSerializedTemporalAction(object, (TemporalAction) src, context);
		}

		// Delegate actions.
		else if (src instanceof DelegateAction) {
			if (src instanceof DelayAction) {
				object = addSerializedDelayAction(object, (DelayAction) src, context);
			} else if (src instanceof RepeatAction) {
				object = addSerializedRepeatAction(object, (RepeatAction) src, context);
			} else if (src instanceof AfterAction) {
				object.addProperty("type", "after");
			}
			object = addSerializedDelegateAction(object, (DelegateAction) src, context);
		}

		// Parallel actions.
		else if (src instanceof ParallelAction) {
			if (src instanceof SequenceAction) {
				object.addProperty("type", "sequence");
			} else {
				object.addProperty("type", "parallel");
			}
			object = addSerializedParallelAction(object, (ParallelAction) src, context);
		}

		else if (src instanceof VisibleAction) {
			object = addSerializedVisibleAction(object, (VisibleAction) src, context);
		}

		else if (src instanceof TouchableAction) {
			object = addSerializedTouchableAction(object, (TouchableAction) src, context);
		}

		else if (src instanceof RemoveActorAction) {
			object.addProperty("type", "remove");
		}

		else if (src instanceof RunnableAction) {
			object = addSerializedRunnableAction(object, (RunnableAction) src, context);
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
		object.addProperty("x", action.getX());
		object.addProperty("y", action.getY());
		return object;
	}

	private JsonObject addSerializedMoveByAction(JsonObject object, MoveByAction action, JsonSerializationContext context) {
		object.addProperty("amountX", action.getAmountX());
		object.addProperty("amountY", action.getAmountY());
		return object;
	}

	private JsonObject addSerializedSizeToAction(JsonObject object, SizeToAction action, JsonSerializationContext context) {
		object.addProperty("width", action.getWidth());
		object.addProperty("height", action.getHeight());
		return object;
	}

	private JsonObject addSerializedSizeByAction(JsonObject object, SizeByAction action, JsonSerializationContext context) {
		object.addProperty("amountWidth", action.getAmountWidth());
		object.addProperty("amountHeight", action.getAmountHeight());
		return object;
	}

	private JsonObject addSerializedRotateToAction(JsonObject object, RotateToAction action, JsonSerializationContext context) {
		object.addProperty("angle", action.getRotation());
		return object;
	}

	private JsonObject addSerializedRotateByAction(JsonObject object, RotateByAction action, JsonSerializationContext context) {
		object.addProperty("rotation", action.getAmount());
		return object;
	}

	private JsonObject addSerializedColorAction(JsonObject object, ColorAction action, JsonSerializationContext context) {
		object.add("color", context.serialize(action.getEndColor()));
		return object;
	}

	private JsonObject addSerializedAlphaAction(JsonObject object, AlphaAction action, JsonSerializationContext context) {
		object.addProperty("alpha", action.getAlpha());
		return object;
	}

	private JsonObject addSerializedVisibleAction(JsonObject object, VisibleAction action, JsonSerializationContext context) {
		object.addProperty("visible", action.isVisible());
		return object;
	}

	private JsonObject addSerializedTouchableAction(JsonObject object, TouchableAction action, JsonSerializationContext context) {
		object.add("touchable", context.serialize(action.getTouchable()));
		return object;
	}

	private JsonObject addSerializedDelegateAction(JsonObject object, DelegateAction action, JsonSerializationContext context) {
		Action delegatedAction = action.getAction();
		if (delegatedAction != null)
			object.add("action",context.serialize(delegatedAction));

		return object;
	}

	private JsonObject addSerializedDelayAction(JsonObject object, DelayAction action, JsonSerializationContext context) {
		object.addProperty("delay", action.getDuration());
		return object;
	}

	private JsonObject addSerializedRepeatAction(JsonObject object, RepeatAction action, JsonSerializationContext context) {
		object.addProperty("count", action.getCount());
		return object;
	}

	private JsonObject addSerializedParallelAction(JsonObject object, ParallelAction action, JsonSerializationContext context) {
		object.add("actions", context.serialize(action.getActions()));
		return object;
	}

	private JsonObject addSerializedTemporalAction(JsonObject object, TemporalAction action, JsonSerializationContext context) {
		object.addProperty("duration", action.getDuration());
		object.add("interpolation", context.serialize(action.getInterpolation()));
		return object;
	}

	// TODO: Serializing a Runnable may not work.
	private JsonObject addSerializedRunnableAction(JsonObject object, RunnableAction action, JsonSerializationContext context) {
		object.add("runnable", context.serialize(action.getRunnable()));
		return object;
	}
}
