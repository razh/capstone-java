package org.capstone.game.input;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.PhysicsActor;
import org.capstone.game.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
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
		Vector2 point = screenToStageCoordinates(screenX, screenY);
		

		if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			Actor hit = State.getStage().getEntities().hit(point.x, point.y, true);
			if (hit != null && hit instanceof MeshActor) {
				State.getPlayer().setSelected(hit);
			}
		} else {
			State.getStage().getEntities().getChildren().get(0).setPosition(point.x, point.y);
		}		

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		State.getPlayer().setSelected(null);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!State.running)
			return true;

		Vector2 point = screenToStageCoordinates(screenX, screenY);
		
		MeshActor selected = (MeshActor) State.getPlayer().getSelected();
		if (State.getPlayer().getSelected() != null) {
			if (selected.getEntity().getTeam() == State.getPlayer().getTeam()) {
				selected.setPosition(point.x, point.y);
				selected.setVelocity(0.0f, 0.0f);
			}
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
	
	private Vector2 screenToStageCoordinates(float screenX, float screenY) {
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
		
		return new Vector2(x, y);
	}

}
