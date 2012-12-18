package org.capstone.game.tests;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import org.capstone.game.MeshStage;
import org.capstone.game.TextMeshActor;
import org.capstone.game.TextMeshGroup;

import com.badlogic.gdx.graphics.Color;

public class TextStageTest extends StageTest {

	@Override
	public void load(MeshStage stage) {
		float textWidth = 30;
		float textHeight = 30;
		float textLineWidth = 3.5f;
		Color textColor = new Color(0.941f, 0.941f, 0.827f, 0.75f);
		stage.addText(new TextMeshActor('0',  40, 100, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('1',  80, 100, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('2',  40, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('3',  80, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('4', 120, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('5', 160, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('6', 200, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('7', 240, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('8', 280, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('9', 320, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('A', 360, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('B', 400, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('C', 440, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('D', 480, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('E', 520, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('F', 560, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('G', 600, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('H', 640, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('I', 680, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('J', 720, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('K', 760, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('L', 800, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('M', 840, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('N', 880, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('O', 920, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('P', 960, 200, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('Q',  40, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('R',  80, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('S', 120, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('T', 160, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('U', 200, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('V', 240, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('W', 280, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('X', 320, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('Y', 360, 300, textColor, textWidth, textHeight, textLineWidth));
		stage.addText(new TextMeshActor('Z', 400, 300, textColor, textWidth, textHeight, textLineWidth));
		TextMeshGroup quickfox = new TextMeshGroup("THE QUICK BROWN FOX", 200, 100, new Color(0.2f, 0.4f, 0.3f, 1.0f), 30, 50, 10, 4.0f);
		quickfox.setName("FOX");
		stage.addText(quickfox);
	}
}
