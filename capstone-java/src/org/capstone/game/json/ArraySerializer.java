package org.capstone.game.json;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ArraySerializer implements JsonSerializer<Array<?>> {

	@Override
	public JsonElement serialize(Array<?> src, Type typeOfSrc,
			JsonSerializationContext context) {

		ArrayList<Object> array = new ArrayList<Object>();
		Object object;

		for (int i = 0, n = src.size; i < n; i++) {
			object = src.get(i);
			if (object != null)
				array.add(object);
		}
	
		return context.serialize(array);
	}

}
