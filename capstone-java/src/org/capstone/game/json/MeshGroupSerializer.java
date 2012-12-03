package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.MeshGroup;
import org.capstone.game.MeshStage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MeshGroupSerializer implements JsonSerializer<MeshGroup> {

	@Override
	public JsonElement serialize(MeshGroup src, Type typeOfSrc,
			JsonSerializationContext context) {
		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new MeshStageExclusionStrategy())
			.registerTypeAdapter(SnapshotArray.class, new SnapshotArraySerializer())
			.serializeNulls()
			.create();
//		JsonArray array = new JsonArray();
		JsonObject object = new JsonObject();
		object.add("children", gson.toJsonTree(src.getChildren()));
		return object;


//		return array;
	}

}
