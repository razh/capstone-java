package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.MeshGroup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MeshGroupSerializer implements JsonSerializer<MeshGroup> {

	@Override
	public JsonElement serialize(MeshGroup src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		object.add("children", context.serialize((src.getChildren())));
		return object;
	}

}
