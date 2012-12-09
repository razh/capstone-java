package org.capstone.game.json;

import java.lang.reflect.Type;

import org.capstone.game.entities.weapons.BulletGun;
import org.capstone.game.entities.weapons.LaserGun;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Color;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class WeaponAdapter implements JsonSerializer<Weapon>, JsonDeserializer<Weapon> {

	@Override
	public Weapon deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject object = json.getAsJsonObject();
		float damage = object.get("damage").getAsFloat();
		float rate = object.get("rate").getAsFloat();
		float range = object.get("range").getAsFloat();

		Weapon weapon = null;
		if (object.get("laserWidth") != null) {
			Color color = context.deserialize(object.get("color"), Color.class);
			float width = object.get("laserWidth").getAsFloat();
			weapon = new LaserGun(damage, rate, range, color, width);
		} else if (object.get("bulletRange") != null) {
			float speed = object.get("speed").getAsFloat();
			Color color = context.deserialize(object.get("color"), Color.class);
			float radius = object.get("radius").getAsFloat();
			float bulletRange = object.get("bulletRange").getAsFloat();
			weapon = new BulletGun(damage, rate, range, speed, color, radius, bulletRange);
		}

		return weapon;
	}

	@Override
	public JsonElement serialize(Weapon src, Type typeOfSrc,
			JsonSerializationContext context) {
		JsonObject object = new JsonObject();
		
		object.addProperty("damage", src.getDamage());
		object.addProperty("rate", src.getRate());
		object.addProperty("range", src.getRange());
		
		if (src instanceof LaserGun) {
			LaserGun gun = (LaserGun) src;
			object.add("color", context.serialize(gun.getColor()));
			object.addProperty("laserWidth", gun.getLaserWidth());
		} else if (src instanceof BulletGun) {
			BulletGun gun = (BulletGun) src;
			
			object.addProperty("speed", gun.getSpeed());
			object.add("color", context.serialize(gun.getColor()));
			object.addProperty("radius", gun.getRadius());
			object.addProperty("bulletRange", gun.getBulletRange());
		}

		return object;
	}

}
