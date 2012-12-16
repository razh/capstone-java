package org.capstone.game.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.badlogic.gdx.scenes.scene2d.actions.*;

// This is ugly, ugly code.
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

		else if (src instanceof AddAction) {
			object.addProperty("type", "add");
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

		JsonObject object = json.getAsJsonObject();

		Action action = null;
		if (object.get("type") != null) {
			String type = object.get("type").getAsString();
			if (type.equals("after")) {
				action = Actions.action(AfterAction.class);
			} else if (type.equals("sequence")) {
				action = Actions.action(SequenceAction.class);
				action = addDeserializedParallelAction(object, (ParallelAction) action, context);
			} else if (type.equals("parallel")) {
				action = Actions.action(ParallelAction.class);
				action = addDeserializedParallelAction(object, (ParallelAction) action, context);
			} else if (type.equals("add")) {
				action = Actions.action(AddAction.class);
			} else if (type.equals("remove")) {
				action = Actions.action(RemoveActorAction.class);
			}
		}

		// MoveToAction.
		else if (object.get("x") != null) {
			action = Actions.action(MoveToAction.class);
			action = addDeserializedMoveToAction(object, (MoveToAction) action, context);
		}
		// MoveByAction.
		else if (object.get("amountX") != null) {
			action = Actions.action(MoveByAction.class);
			action = addDeserializedMoveByAction(object, (MoveByAction) action, context);
		}

		// SizeToAction.
		else if (object.get("width") != null) {
			action = Actions.action(SizeToAction.class);
			action = addDeserializedSizeToAction(object, (SizeToAction) action, context);
		}

		// SizeByAction.
		else if (object.get("amountWidth") != null) {
			action = Actions.action(SizeByAction.class);
			action = addDeserializedSizeByAction(object, (SizeByAction) action, context);
		}

		// ColorAction.
		else if (object.get("color") != null) {
			action = Actions.action(ColorAction.class);
			action = addDeserializedColorAction(object, (ColorAction) action, context);
		}

		// AlphaAction.
		else if (object.get("alpha") != null) {
			action = Actions.action(AlphaAction.class);
			action = addDeserializedAlphaAction(object, (AlphaAction) action, context);
		}

		// RotateToAction.
		else if (object.get("angle") != null) {
			action = Actions.action(RotateToAction.class);
			action = addDeserializedRotateToAction(object, (RotateToAction) action, context);
		}

		// RotateByAction.
		else if (object.get("rotation") != null) {
			action = Actions.action(RotateByAction.class);
			action = addDeserializedRotateByAction(object, (RotateByAction) action, context);
		}

		// DelayAction.
		else if (object.get("delay") != null) {
			action = Actions.action(DelayAction.class);
			action = addDeserializedDelayAction(object, (DelayAction) action, context);
		}

		// RepeatAction.
		else if (object.get("count") != null) {
			action = Actions.action(RepeatAction.class);
			action = addDeserializedRepeatAction(object, (RepeatAction) action, context);
		}

		// VisibleAction.
		else if (object.get("visible") != null) {
			action = Actions.action(VisibleAction.class);
			action = addDeserializedVisibleAction(object, (VisibleAction) action, context);
		}

		// TouchableAction.
		else if (object.get("touchable") != null) {
			action = Actions.action(TouchableAction.class);
			action = addDeserializedTouchableAction(object, (TouchableAction) action, context);
		}

		return action;
	}

	private JsonObject addSerializedMoveToAction(JsonObject object, MoveToAction action, JsonSerializationContext context) {
		object.addProperty("x", action.getX());
		object.addProperty("y", action.getY());
		return object;
	}

	private MoveToAction addDeserializedMoveToAction(JsonObject object, MoveToAction action, JsonDeserializationContext context) {
		float x = object.get("x").getAsFloat();
		float y = object.get("y").getAsFloat();

		action.setX(x);
		action.setX(y);

		action = (MoveToAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedMoveByAction(JsonObject object, MoveByAction action, JsonSerializationContext context) {
		object.addProperty("amountX", action.getAmountX());
		object.addProperty("amountY", action.getAmountY());
		return object;
	}

	private MoveByAction addDeserializedMoveByAction(JsonObject object, MoveByAction action, JsonDeserializationContext context) {
		float amountX = object.get("amountX").getAsFloat();
		float amountY = object.get("amountY").getAsFloat();

		action.setAmountX(amountX);
		action.setAmountY(amountY);

		action = (MoveByAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedSizeToAction(JsonObject object, SizeToAction action, JsonSerializationContext context) {
		object.addProperty("width", action.getWidth());
		object.addProperty("height", action.getHeight());
		return object;
	}

	private SizeToAction addDeserializedSizeToAction(JsonObject object, SizeToAction action, JsonDeserializationContext context) {
		float width = object.get("width").getAsFloat();
		float height = object.get("height").getAsFloat();

		action.setWidth(width);
		action.setHeight(height);

		action = (SizeToAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedSizeByAction(JsonObject object, SizeByAction action, JsonSerializationContext context) {
		object.addProperty("amountWidth", action.getAmountWidth());
		object.addProperty("amountHeight", action.getAmountHeight());
		return object;
	}

	private SizeByAction addDeserializedSizeByAction(JsonObject object, SizeByAction action, JsonDeserializationContext context) {
		float amountWidth = object.get("amountWidth").getAsFloat();
		float amountHeight = object.get("amountHeight").getAsFloat();

		action.setAmountWidth(amountWidth);
		action.setAmountHeight(amountHeight);

		action = (SizeByAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedRotateToAction(JsonObject object, RotateToAction action, JsonSerializationContext context) {
		object.addProperty("angle", action.getRotation());
		return object;
	}

	private RotateToAction addDeserializedRotateToAction(JsonObject object, RotateToAction action, JsonDeserializationContext context) {
		float angle = object.get("angle").getAsFloat();

		action.setRotation(angle);
		action = (RotateToAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedRotateByAction(JsonObject object, RotateByAction action, JsonSerializationContext context) {
		object.addProperty("rotation", action.getAmount());
		return object;
	}

	private RotateByAction addDeserializedRotateByAction(JsonObject object, RotateByAction action, JsonDeserializationContext context) {
		float rotation = object.get("rotation").getAsFloat();

		action.setAmount(rotation);
		action = (RotateByAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedColorAction(JsonObject object, ColorAction action, JsonSerializationContext context) {
		object.add("color", context.serialize(action.getEndColor()));
		return object;
	}

	private ColorAction addDeserializedColorAction(JsonObject object, ColorAction action, JsonDeserializationContext context) {
		JsonObject colorObject = object.get("color").getAsJsonObject();

		float r = colorObject.get("r").getAsFloat();
		float g = colorObject.get("g").getAsFloat();
		float b = colorObject.get("b").getAsFloat();
		float a = colorObject.get("a").getAsFloat();

		action.setEndColor(new Color(r, g, b, a));

		action = (ColorAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedAlphaAction(JsonObject object, AlphaAction action, JsonSerializationContext context) {
		object.addProperty("alpha", action.getAlpha());
		return object;
	}

	private AlphaAction addDeserializedAlphaAction(JsonObject object, AlphaAction action, JsonDeserializationContext context) {
		float alpha = object.get("alpha").getAsFloat();

		action.setAlpha(alpha);
		action = (AlphaAction) addDeserializedTemporalAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedVisibleAction(JsonObject object, VisibleAction action, JsonSerializationContext context) {
		object.addProperty("visible", action.isVisible());
		return object;
	}

	private VisibleAction addDeserializedVisibleAction(JsonObject object, VisibleAction action, JsonDeserializationContext context) {
		boolean visible = object.get("visible").getAsBoolean();

		action.setVisible(visible);

		return action;
	}

	private JsonObject addSerializedTouchableAction(JsonObject object, TouchableAction action, JsonSerializationContext context) {
		object.add("touchable", context.serialize(action.getTouchable()));
		return object;
	}

	private TouchableAction addDeserializedTouchableAction(JsonObject object, TouchableAction action, JsonDeserializationContext context) {
		Touchable touchable = context.deserialize(object.get("touchable"), Touchable.class);

		action.setTouchable(touchable);

		return action;
	}

	private JsonObject addSerializedDelegateAction(JsonObject object, DelegateAction action, JsonSerializationContext context) {
		Action delegatedAction = action.getAction();
		if (delegatedAction != null)
			object.add("action", context.serialize(delegatedAction));

		return object;
	}

	private DelegateAction addDeserializedDelegateAction(JsonObject object, DelegateAction action, JsonDeserializationContext context) {
		Action delegatedAction = context.deserialize(object.get("action"), Action.class);

		action.setAction(delegatedAction);

		return action;
	}

	private JsonObject addSerializedDelayAction(JsonObject object, DelayAction action, JsonSerializationContext context) {
		object.addProperty("delay", action.getDuration());
		return object;
	}

	private DelayAction addDeserializedDelayAction(JsonObject object, DelayAction action, JsonDeserializationContext context) {
		float delay = object.get("delay").getAsFloat();

		action.setTime(delay);
		action = (DelayAction) addDeserializedDelegateAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedRepeatAction(JsonObject object, RepeatAction action, JsonSerializationContext context) {
		object.addProperty("count", action.getCount());
		return object;
	}

	private RepeatAction addDeserializedRepeatAction(JsonObject object, RepeatAction action, JsonDeserializationContext context) {
		int count = object.get("count").getAsInt();

		action.setCount(count);
		action = (RepeatAction) addDeserializedDelegateAction(object, action, context);

		return action;
	}

	private JsonObject addSerializedParallelAction(JsonObject object, ParallelAction action, JsonSerializationContext context) {
		object.add("actions", context.serialize(action.getActions()));
		return object;
	}

	private ParallelAction addDeserializedParallelAction(JsonObject object, ParallelAction action, JsonDeserializationContext context) {
		JsonArray jsonActions = object.get("actions").getAsJsonArray();

		Action tempAction;
		for (int i = 0, n = jsonActions.size(); i < n; i++) {
			tempAction = (Action) context.deserialize(jsonActions.get(i), Action.class);
			if (tempAction != null) {
				action.addAction(tempAction);
			} else {
				System.out.println("Error: Action could not be added to ParallelAction: " + jsonActions.get(i));
			}
		}

		return action;
	}

	private JsonObject addSerializedTemporalAction(JsonObject object, TemporalAction action, JsonSerializationContext context) {
		object.addProperty("duration", action.getDuration());
		object.add("interpolation", context.serialize(action.getInterpolation()));
		return object;
	}

	private TemporalAction addDeserializedTemporalAction(JsonObject object, TemporalAction action, JsonDeserializationContext context) {
		float duration = object.get("duration").getAsFloat();
		Interpolation interpolation = context.deserialize(object.get("interpolation"), Interpolation.class);

		action.setDuration(duration);
		action.setInterpolation(interpolation);

		return action;
	}

	// TODO: Serializing a Runnable may not work.
	private JsonObject addSerializedRunnableAction(JsonObject object, RunnableAction action, JsonSerializationContext context) {
		object.add("runnable", context.serialize(action.getRunnable()));
		return object;
	}
}
