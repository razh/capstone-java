package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshType;
import org.capstone.game.entities.Entity;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class EntityDeserializer implements JsonDeserializer<Entity> {

	@Override
	public Entity deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		// Check if MeshActor exist;
		if (object.get("actor") == null)
			return null;

		String meshType = object.get("meshType").getAsString();
		MeshType type = null;
		if (meshType.equals("CircleMeshActor")) {
			type = MeshType.CircleMeshActor;
		} else if (meshType.equals("RectMeshActor")) {
			type = MeshType.RectMeshActor;
		} else if (meshType.equals("PolygonMeshActor")) {
			type = MeshType.PolygonMeshActor;
		}

		int team = object.get("team").getAsInt();
		int initialHealth = object.get("initialHealth").getAsInt();
		int health = object.get("health").getAsInt();
		boolean immortal = object.get("immortal").getAsBoolean();
		boolean alive = object.get("alive").getAsBoolean();
		float lifeTime = object.get("lifeTime").getAsFloat();
		boolean oriented = object.get("oriented").getAsBoolean();

		// Get MeshActor fields.
		JsonObject jsonActor = object.get("actor").getAsJsonObject();

		float x = jsonActor.get("x").getAsFloat();
		float y = jsonActor.get("y").getAsFloat();

		float r = jsonActor.get("color").getAsJsonObject().get("r").getAsFloat();
		float g = jsonActor.get("color").getAsJsonObject().get("g").getAsFloat();
		float b = jsonActor.get("color").getAsJsonObject().get("b").getAsFloat();
		float a = jsonActor.get("color").getAsJsonObject().get("a").getAsFloat();

		float width  = jsonActor.get("width").getAsFloat();
		float height = jsonActor.get("height").getAsFloat();
		float rotation = jsonActor.get("rotation").getAsFloat();

		float velocityX = jsonActor.get("velocityX").getAsFloat();
		float velocityY = jsonActor.get("velocityY").getAsFloat();

		JsonArray jsonActions = jsonActor.get("actions").getAsJsonArray();

		// Create the Entity.
		Entity entity = new Entity(type, x, y, new Color(r, g, b, a), width, height);
		entity.setTeam(team);
		entity.setInitialHealth(initialHealth);
		entity.setHealth(health);
		entity.setImmortal(immortal);
		entity.setAlive(alive);
		entity.setLifeTime(lifeTime);
		entity.setOriented(oriented);

		JsonArray jsonWeapons = object.get("weapons").getAsJsonArray();
		Weapon weapon;
		for (int i = 0, n = jsonWeapons.size(); i < n; i++) {
			weapon = context.deserialize(jsonWeapons.get(i), Weapon.class);
			if (weapon != null)
				entity.addWeapon(weapon);
		}

		// Add actions.
		MeshActor actor = entity.getMeshActor();
		actor.setRotation(rotation);
		actor.setVelocity(velocityX, velocityY);

		Action action;
		for (int i = 0, n = jsonActions.size(); i < n; i++) {
			action = (Action) context.deserialize(jsonActions.get(i), Action.class);
			if (action != null) {
				actor.addAction(action);
			} else {
				System.out.println("Error: Action could not be added to MeshActor: " + jsonActions.get(i));
			}
		}

		return entity;
	}

	public static Entity deserializeEntity(JsonElement json, JsonDeserializationContext context) {
		return null;
	}
}
