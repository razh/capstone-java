package org.capstone.game.entities;

import org.capstone.game.graphics.GraphicsComponent;
import org.capstone.game.physics.PhysicsComponent;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Character {
	private GraphicsComponent graphics;
	private PhysicsComponent physics;
	
	private int team = 0;
	
	public Character() {
		
	}
	
	public void update(double elapsedTime) {
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
