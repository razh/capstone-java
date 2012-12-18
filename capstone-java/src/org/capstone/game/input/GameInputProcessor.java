package org.capstone.game.input;

import org.capstone.game.MeshGroup;
import org.capstone.game.PhysicsActor;
import org.capstone.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameInputProcessor implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!State.running)
			return true;

		float x, y;
		if (Gdx.graphics.getWidth() != State.getWidth()) {
			x = screenX * (State.getWidth() / Gdx.graphics.getWidth());
		} else {
			x = screenX;
		}
		
		if (Gdx.graphics.getHeight() != State.getHeight()) {
			y = (Gdx.graphics.getHeight() - screenY) * (State.getHeight() / Gdx.graphics.getHeight());
		} else {
			y = Gdx.graphics.getHeight() - screenY;
		}

		if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			Actor hit = State.getStage().getEntities().hit(x, y, true);
			if (hit != null) {
				hit.setPosition(x, y);
				((PhysicsActor) hit).setVelocity(0.0f, 0.0f);
			}
			else {
				hit = State.getStage().getText().hit(x, y, true);
				if (hit != null) {
					hit.setPosition(x, y);
					if (hit instanceof PhysicsActor)
						((PhysicsActor) hit).setVelocity(0.0f, 0.0f);
					else if (hit instanceof MeshGroup) {
						((MeshGroup) hit).setVelocity(0.0f, 0.0f);
					}
				}
			}
		} else {
			State.getStage().getEntities().getChildren().get(0).setPosition(x, y);
		}		

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
