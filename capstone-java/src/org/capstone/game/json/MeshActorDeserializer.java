package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.MeshActor;
import org.capstone.game.TextMeshActor;
import org.capstone.game.entities.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class MeshActorDeserializer implements JsonDeserializer<MeshActor> {

	@Override
	public MeshActor deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		MeshActor actor = null;
		// Handle TextMeshActors.
		if (object.get("character") != null) {
			float x = object.get("x").getAsFloat();
			float y = object.get("y").getAsFloat();

			float r = object.get("color").getAsJsonObject().get("r").getAsFloat();
			float g = object.get("color").getAsJsonObject().get("g").getAsFloat();
			float b = object.get("color").getAsJsonObject().get("b").getAsFloat();
			float a = object.get("color").getAsJsonObject().get("a").getAsFloat();

			float width  = object.get("width").getAsFloat();
			float height = object.get("height").getAsFloat();
			float rotation = object.get("rotation").getAsFloat();

			float velocityX = object.get("velocityX").getAsFloat();
			float velocityY = object.get("velocityY").getAsFloat();

			char c = object.get("character").getAsCharacter();
			actor = new TextMeshActor(c, x, y, new Color(r, g, b, a), width, height);

			actor.setRotation(rotation);
			actor.setVelocity(velocityX, velocityY);

			JsonArray jsonActions = object.get("actions").getAsJsonArray();
			Action action;
			for (int i = 0, n = jsonActions.size(); i < n; i++) {
				action = (Action) context.deserialize(jsonActions.get(i), Action.class);
				if (action != null) {
					actor.addAction(action);
				} else {
					System.out.println("Error: Action could not be added to MeshActor: " + jsonActions.get(i));
				}
			}
		} else if (object.get("entity") != null) {
			JsonObject jsonEntity = object.get("entity").getAsJsonObject();
			Entity entity = EntityDeserializer.deserializeEntityAndActor(jsonEntity, object, context);
			actor = entity.getMeshActor();
		}

		return actor;
	}
}
