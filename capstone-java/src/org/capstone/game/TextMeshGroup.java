package org.capstone.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class TextMeshGroup extends MeshGroup {
	public static enum Alignment {
		LEFT,
		CENTER,
		RIGHT
	};

	private String text;
	private float spacing;
	private Array<TextMeshActor> textActors = new Array<TextMeshActor>();
	private float lineWidth = 1.0f;
	private Alignment alignment = Alignment.RIGHT;

	public TextMeshGroup(String text, float x, float y, Color color, float width, float height, float spacing, float lineWidth) {
		setText(text);
		setPosition(x, y);
		setColor(color);
		setWidth(width);
		setHeight(height);
		setSpacing(spacing);
		setLineWidth(lineWidth);

		// Create all instances of TextMeshActors first, since setPosition() is overwritten.
		initText();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void initText() {
		float x = getX();
		if (alignment == Alignment.RIGHT) {
			x -= text.length() * (getWidth() + spacing);
		} else if (alignment == Alignment.CENTER) {
			x -= (text.length() * (getWidth() + spacing)) * 0.5f;
		}

		float y = getY();
		for (int i = 0; i < text.length(); i++) {
			if (Character.isLetterOrDigit(text.charAt(i))) {
				TextMeshActor textActor = new TextMeshActor(text.charAt(i), x, y, getColor(), getWidth(), getHeight(), getLineWidth());
				textActors.add(textActor);
				addActor(textActor);
			}

			x += getWidth() + spacing;
		}
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	// Only add TextMeshActors.
	public void addActor(Actor actor) {
		if (actor instanceof TextMeshActor) {
			super.addActor(actor);
		}
	}

	public void act(float delta) {
		super.act(delta);

		if (textActors.size > 0) {
			int index = 0;
			float x = textActors.get(0).getX();
			float y = textActors.get(0).getY();
			for (int i = 0; i < text.length(); i++) {
				if (Character.isLetterOrDigit(text.charAt(i))) {
					textActors.get(index).setX(x);
					textActors.get(index).setY(y);

					index++;
				}

				x += getWidth() + spacing;
			}
		}
	}
	
	public TextMeshActor getActorAt(int index) {
		return textActors.get(index);
	}

	public Actor hit(float x, float y, boolean touchable) {
		if (super.hit(x, y, touchable) != null) {
			return this;
		}

		return null;
	}
	
	@Override
	public void setPosition(float x, float y) {
		if (textActors.size == 0) {
			super.setPosition(x, y);
			return;
		}
			
		if (alignment == Alignment.LEFT) {
			textActors.get(0).setPosition(x, y);
		} else if (alignment == Alignment.CENTER) {
			textActors.get(0).setPosition(x - ((text.length() - 1) * (getWidth() + spacing)) * 0.5f, y);
		} else if (alignment == Alignment.RIGHT) {
			textActors.get(0).setPosition(x - (text.length() - 1) * (getWidth() + spacing), y);
		}
	}
}