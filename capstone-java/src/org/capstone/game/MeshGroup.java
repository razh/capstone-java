package org.capstone.game;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class MeshGroup extends Group {
	protected final Matrix4 currTransform = new Matrix4();
	protected final Matrix4 prevTransform = new Matrix4();
	protected boolean transform = true;
	protected Rectangle cullingArea;
	protected ShaderProgram shaderProgram;

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (shaderProgram != null) {
			draw(parentAlpha);
		}
	}

	public void draw(ShaderProgram shaderProgram, float parentAlpha) {
		this.shaderProgram = shaderProgram;
	}

	public void draw(float parentAlpha) {
		if (shaderProgram == null) {
			return;
		}

		if (transform) {
		}
	}

	protected void applyTransform(Matrix4 transform) {

	}

	protected void resetTransform() {
	}

	protected void drawChildren(float parentAlpha) {
		parentAlpha *= this.color.a;
		List<Actor> children = this.children;
		Rectangle cullingArea = this.cullingArea;

		if (cullingArea != null) {
			// Draw only children inside culling area.
			if (transform) {
//				for (int i = 0;)

			} else {
				// No transform for this group, offset children.

			}
		} else {
			// No culling, draw all children.
			if (transform) {

			} else {
				// No transform for this group, offset children.
			}
		}
	}
	
	
	public void setStage(MeshStage stage) {
		this.stage = stage;
	}
}
