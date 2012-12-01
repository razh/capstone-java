package org.capstone.game.entities.weapons;

public class WeaponFactory {
	public static Weapon createWeapon(Weapon weapon) {
		Weapon newWeapon = null;

		if (weapon instanceof BulletGun) {
			newWeapon = new BulletGun((BulletGun) weapon);
		} else if (weapon instanceof LaserGun) {
			newWeapon = new LaserGun((LaserGun) weapon);
		}

		return newWeapon;
	}
}
