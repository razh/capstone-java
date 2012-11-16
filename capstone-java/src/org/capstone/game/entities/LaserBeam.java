package org.capstone.game.entities;

import org.capstone.game.MeshActor;
import org.capstone.game.RectMeshActor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LaserBeam extends RectMeshActor {
	protected MeshActor actor;
	
	public LaserBeam(Actor actor, float x, float y, Color color, float width) {
		setActor((MeshActor) actor);
		setPosition(x, y);
		setColor(color);
	}

	public MeshActor getActor() {
		return actor;
	}

	public void setActor(MeshActor actor) {
		this.actor = actor;
	}
}
