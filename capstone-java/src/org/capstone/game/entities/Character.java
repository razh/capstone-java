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
		this.graphics = new CircleGraphicsComponent(0, 0, color, radius);
		this.physics  = new CirclePhysicsComponent(x, y, radius);
	}

	public void update(long elapsedTime) {
		this.physics.update(elapsedTime);
	}

	public void render(ShaderProgram shaderProgram) {
		shaderProgram.setUniformf("translate", this.physics.getPosition());
		this.graphics.render(shaderProgram);
	}

	public int getTeam() {
		return this.team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
}
