package org.capstone.game.entities.weapons;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Gun extends Weapon {

	public Gun(Actor actor, float damage, float rate, float range) {
		super(actor, damage, rate, range);
	}
	
	public Gun(Gun gun) {
		super(gun);
	}
	
	public void act(float delta) {
		super.act(delta);
		
		if (isFiring() && hasTarget() && targetInRange()) {
			fire();
		} else {
			setFiring(false);
		}
	}
	
	public void fire() {};
}
