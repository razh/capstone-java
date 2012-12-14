package org.capstone.game.io;

import org.capstone.game.Level;
import org.capstone.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;

public class LevelLoader {
	private Gson gson;
	private String[] levelNames = {
		"testlevel.json"	
	};
	private Level level;
	
	public LevelLoader(Gson gson) {
		setGson(gson);
	}
	
	public LevelLoader() {
		level = new Level();
		State.setLevel(level);
		
		try {
			FileHandle file;
			for (int i = 0; i < levelNames.length; i++) {
				 file = Gdx.files.internal(levelNames[i]);
				 
				 System.out.println(file.readString());
			}
		} catch (GdxRuntimeException e) {
			e.printStackTrace();
		}
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void act(float delta) {
		
	}

}
