package org.capstone.game.tests;

import org.capstone.game.MeshStage;
import org.capstone.game.State;
import org.capstone.game.entities.CircleEntity;
import org.capstone.game.entities.Entity;
import org.capstone.game.entities.weapons.BulletGun;
import org.capstone.game.entities.weapons.LaserGun;

import com.badlogic.gdx.graphics.Color;

public class SimpleGameStageTest extends StageTest {

	@Override
	public void load(MeshStage stage) {
		float width = State.getWidth();
		float height = State.getHeight();

		Entity redCircle = new CircleEntity(width * 0.75f, height * 0.6f, new Color(0.941f, 0.247f, 0.208f, 1.0f), 60);
		redCircle.addWeapon(new LaserGun(redCircle, 1.0f, 0.2f, 400.0f, new Color(0.941f, 0.404f, 0.365f, 0.75f), 1.5f));
		redCircle.setImmortal(true);

		Entity blueCircle = new CircleEntity(width * 0.75f, height * 0.3f, new Color(0.173f, 0.204f, 0.220f, 1.0f), 60);
		blueCircle.addWeapon(new BulletGun(blueCircle, 1.0f, 0.15f, -1.0f, 600.0f, new Color(0.106f, 0.126f, 0.146f, 1.0f), 4.0f));
		((BulletGun) blueCircle.getWeapons().get(0)).setBulletRange(600.0f);
		
		stage.addEntity(redCircle);
		stage.addEntity(blueCircle);
	}

}
