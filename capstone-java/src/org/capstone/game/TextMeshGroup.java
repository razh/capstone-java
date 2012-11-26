package org.capstone.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


public class TextMeshGroup extends MeshGroup {
	private String text;
	private float spacing;
	private Array<TextMeshActor> textActors = new Array<TextMeshActor>();

	public TextMeshGroup(String text, float x, float y, Color color, float width, float height, float spacing) {
		setText(text);
		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);
		setSpacing(spacing);

		for (int i = 0; i < text.length(); i++) {
			if (Character.isLetterOrDigit(text.charAt(i))) {
				TextMeshActor textActor = new TextMeshActor(text.charAt(i), x, y, color, width, height);
				addActor(textActor);
			}
			x += width + spacing;
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	// Only add TextMeshActors.
	public void addActor(Actor actor) {
		if (actor instanceof TextMeshActor) {
			super.addActor(actor);
		}
	}

	public void act(float delta) {
		super.act(delta);

		float x, y;
		for (int i = 0; i < textActors.size - 1; i++) {
			x = textActors.get(i).getX();
			y = textActors.get(i).getY();

			x += getWidth() + spacing;
			textActors.get(i + 1).setX(x);
		}
	}
}
