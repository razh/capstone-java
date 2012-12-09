package org.capstone.game.json;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.badlogic.gdx.math.Interpolation;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class InterpolationAdapter implements JsonSerializer<Interpolation>, JsonDeserializer<Interpolation> {
	private static Field[] fields;

	@Override
	public JsonElement serialize(Interpolation src, Type typeOfSrc,
			JsonSerializationContext context) {

		// Interpolation methods are declared as static variables.
		if (fields == null)
			fields = src.getClass().getFields();

		Field field = null;

		try {
			// Loop through fields to get field name.
			for (int i = 0; i < fields.length; i++) {
				if (src == fields[i].get(null)) {
					field = fields[i];
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (field != null)
			return context.serialize(field.getName());

		return null;
	}

	@Override
	public Interpolation deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		Interpolation interpolation = Interpolation.linear;

		try {
			interpolation = (Interpolation) Interpolation.class.getDeclaredField((json.getAsString())).get(null);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return interpolation;
	}
}
