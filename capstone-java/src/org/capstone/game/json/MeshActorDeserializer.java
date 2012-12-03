package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshType;
import org.capstone.game.entities.Entity;

import com.badlogic.gdx.graphics.Color;
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
		JsonObject jsonEntity = object.get("entity").getAsJsonObject();
		String meshType = jsonEntity.getAsJsonObject().get("meshType").getAsString();
		MeshType type = null;
		if (meshType.equals("CircleMeshActor")) {
			type = MeshType.CircleMeshActor;
		} else if (meshType.equals("RectMeshActor")) {
			type = MeshType.RectMeshActor;
		} else if (meshType.equals("PolygonMeshActor")) {
			type = MeshType.PolygonMeshActor;
		}

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

		Entity entity = new Entity(type, x, y, new Color(r, g, b, a), width, height);

		MeshActor actor = entity.getMeshActor();
		actor.setRotation(rotation);
		actor.setVelocity(velocityX, velocityY);

		return actor;
	}

}
