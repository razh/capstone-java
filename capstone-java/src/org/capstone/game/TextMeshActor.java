package org.capstone.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;

public class TextMeshActor extends MeshActor {
	protected Mesh mesh;
	protected Character character;

	public TextMeshActor(Character character, float x, float y, Color color, float width, float height) {
		super();

		setChar(character);
		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);

		initMesh();
	}

	public Character getChar() {
		return character;
	}

	public void setChar(Character character) {
		this.character = character;
	}

	private void initMesh() {
		switch (character) {
			case 'A':
				break;
		}
	}
}
