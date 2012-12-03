package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.MeshGroup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ActorSerializer implements JsonSerializer<Actor> {

	@Override
	public JsonElement serialize(Actor src, Type typeOfSrc,
			JsonSerializationContext context) {

		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new MeshStageExclusionStrategy())
			.registerTypeAdapter(MeshGroup.class, new MeshGroupSerializer())
			.serializeNulls()
			.create();

		JsonObject object = gson.toJsonTree(src).getAsJsonObject();

		if (src.getListeners().size == 0)
			object.remove("listeners");
		if (src.getCaptureListeners().size == 0)
			object.remove("captureListeners");
		if (src.getActions().size == 0)
			object.remove("actions");
		
		return object;		
	}

}
