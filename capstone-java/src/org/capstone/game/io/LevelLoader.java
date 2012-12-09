package org.capstone.game.io;

import com.google.gson.Gson;

public class LevelLoader {
	private Gson gson;
	
	public LevelLoader(Gson gson) {
		setGson(gson);
	}
	
	public LevelLoader() {}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

}
