package org.capstone.game.ui;

import org.capstone.game.TextMeshActor;
import org.capstone.game.TextMeshGroup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class ScoreCounter extends TextMeshGroup {
	private int score = 0;

	public ScoreCounter(float x, float y, Color color,
	                    float width, float height, float spacing, float lineWidth) {
		super("0", x, y, color, width, height, spacing, lineWidth);
		setAlignment(Alignment.CENTER);

//		Array<TextMeshActor> textActorsArray = getTextActorsArray();
//		for (int i = 0, n = textActorsArray.size; i < n; i++) {
//			textActorsArray.get(i).setBounded(false);
//		}
	}

	public void setScore(int score) {
		setText(Integer.toString(score));
	}
	
	public void addScore(int score) {
		this.score += score;
		setScore(this.score);
	}
}
