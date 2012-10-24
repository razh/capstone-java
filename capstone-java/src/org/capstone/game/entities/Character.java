package org.capstone.game.entities;

import org.capstone.game.graphics.CircleGraphicsComponent;
import org.capstone.game.graphics.GraphicsComponent;
import org.capstone.game.physics.CirclePhysicsComponent;
import org.capstone.game.physics.PhysicsComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Character {
	public GraphicsComponent graphics;
	public PhysicsComponent physics;

	private int team = 0;

	public Character(float x, float y, Color color, float radius) {
		graphics = new CircleGraphicsComponent(0, 0, color, radius);
		physics  = new CirclePhysicsComponent(x, y, radius);
	}

	public void update(long elapsedTime) {
		physics.update(elapsedTime);
	}

	public void render(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("translate", physics.getPosition());
		graphics.render(shaderProgram);
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
}
