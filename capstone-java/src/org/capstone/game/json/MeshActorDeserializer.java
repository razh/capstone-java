package org.capstone.game.json;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshType;
import org.capstone.game.TextMeshActor;
import org.capstone.game.entities.Entity;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
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

		JsonArray jsonActions = object.get("actions").getAsJsonArray();

		MeshActor actor;
		// Handle TextMeshActors.
		if (object.get("character") != null) {
			char c = object.get("character").getAsCharacter();
			actor = new TextMeshActor(c, x, y, new Color(r, g, b, a), width, height);
		} else {
			// Handle other MeshActors.
			JsonObject jsonEntity = object.get("entity").getAsJsonObject();
			String meshType = jsonEntity.get("meshType").getAsString();
			MeshType type = null;
			if (meshType.equals("CircleMeshActor")) {
				type = MeshType.CircleMeshActor;
			} else if (meshType.equals("RectMeshActor")) {
				type = MeshType.RectMeshActor;
			} else if (meshType.equals("PolygonMeshActor")) {
				type = MeshType.PolygonMeshActor;
			}

			int team = jsonEntity.get("team").getAsInt();
			int health = jsonEntity.get("health").getAsInt();
			boolean oriented = jsonEntity.get("oriented").getAsBoolean();

			Entity entity = new Entity(type, x, y, new Color(r, g, b, a), width, height);
			entity.setTeam(team);
			entity.setHealth(health);
			entity.setOriented(oriented);
			
			// Add weapons.
			JsonArray jsonWeapons = jsonEntity.get("weapons").getAsJsonArray();
			Weapon weapon;
			for (int i = 0, n = jsonWeapons.size(); i < n; i++) {
				weapon = context.deserialize(jsonWeapons.get(i), Weapon.class);
				if (weapon != null)
					entity.addWeapon(weapon);
			}

			actor = entity.getMeshActor();
		}

		if (actor != null) {
			actor.setRotation(rotation);
			actor.setVelocity(velocityX, velocityY);

			Action action;
			for (int i = 0, n = jsonActions.size(); i < n; i++) {
				action = (Action) context.deserialize(jsonActions.get(i), Action.class);
				if (action != null) {
					actor.addAction(action);
				}
			}
		}

		return actor;
	}
}
