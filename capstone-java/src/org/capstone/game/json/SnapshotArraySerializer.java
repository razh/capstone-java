package org.capstone.game.json;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SnapshotArraySerializer implements JsonSerializer<SnapshotArray<?>> {

	@Override
	public JsonElement serialize(SnapshotArray<?> src, Type typeOfSrc,
			JsonSerializationContext context) {

		 ArrayList<Object> array = new ArrayList<Object>();
		 Object object;

		 src.begin();
		 for (int i = 0, n = src.size; i < n; i++) {
		 	object = src.get(i);
		 	if (object != null)
		 		array.add(object);
		 }
		 src.end();

		return context.serialize(array);
	}
}
