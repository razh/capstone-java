package org.capstone.game.json;

import java.lang.reflect.Type;
import java.util.ArrayList;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class SnapshotArrayDeserializer implements JsonSerializer<SnapshotArray> {

	@Override
	public JsonElement serialize(SnapshotArray src, Type typeOfSrc,
			JsonSerializationContext context) {
		Gson gson = new GsonBuilder()
			.setExclusionStrategies(new MeshStageExclusionStrategy())
			.serializeNulls()
			.create();

//		ArrayList<Object> array = new ArrayList<Object>();
//		src.begin();
//		for (int i = 0, n = src.size; i < n; i++) {
//			array.add(src.get(i));
//		}
//		src.end();
		// JsonArray array = new JsonArray();
		 ArrayList<Object> array = new ArrayList<Object>();
		 src.begin();
		 for (int i = 0, n = src.size; i < n; i++) {
		 	// array.add(gson.toJsonTree(src.get(i)));
		 	if (src.get(i) != null)
		 		array.add(src.get(i));
		 }


		// System.out.println("-----");
		// for (int i = 0; i <array.size(); i++) {
		// System.out.println(array.get(i));
		// }
		// System.out.println("-----");

		// return new JsonPrimitive(gson.toJson(array));
		return gson.toJsonTree(array);

//		return new JsonPrimitive(gson.toJson(array));
	}

}
