package org.capstone.game.entities;

import java.util.ArrayList;

import org.capstone.game.CircleMeshActor;
import org.capstone.game.RectMeshActor;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Character extends CircleMeshActor {
	protected int team = 0;
	protected boolean takingFire = false;
	protected ArrayList<Weapon> weapons;

	public Character(float x, float y, Color color, float radius) {
		super();

		setPosition(x, y);
		setColor(color);
		setWidth(radius);
		setHeight(radius);
		
		weapons = new ArrayList<Weapon>();
	}

	public void act(float delta) {
		super.act(delta);
	}
	
	public Actor getNearestActor(Actor[] actors) {
		return null;
	}

	public void takeFire() {
		if (!takingFire) {
			takingFire = true;
			addAction(
				sequence(
					parallel(
						color(new Color(0.96f, 0.204f, 0.220f, 1.0f), 0.05f, Interpolation.pow3),
						sizeBy(20, 20, 0.05f, Interpolation.pow3)
					),
					parallel(
						color(new Color(0.173f, 0.204f, 0.220f, 1.0f), 0.15f, Interpolation.linear),
						sizeBy(-20, -20, 0.15f, Interpolation.linear)
					),
					delay(0.1f),
					new Action() {
						public boolean act(float delta) {
							((Character) this.actor).takingFire = false;
							return true;
						}
					}
				)
			);
		}
	}
	
	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
}
