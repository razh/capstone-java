package org.capstone.game.ui;

import org.capstone.game.TextMeshGroup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Counter extends TextMeshGroup {
	private int number = 0;

	public Counter(float x, float y, Color color,
	                    float width, float height, float spacing, float lineWidth) {
		super("0", x, y, color, width, height, spacing, lineWidth);
		setAlignment(Alignment.CENTER);
		setTouchable(Touchable.disabled);

//		Array<TextMeshActor> textActorsArray = getTextActorsArray();
//		for (int i = 0, n = textActorsArray.size; i < n; i++) {
//			textActorsArray.get(i).setBounded(false);
//		}
	}

	public void set(int number) {
		setText(Integer.toString(number));
	}
	
	public void add(int difference) {
		this.number += difference;
		set(this.number);
	}
}
