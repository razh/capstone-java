package org.capstone.game.entities;

import java.util.ArrayList;

import org.capstone.game.entities.weapons.Weapon;

public interface Weaponable {
	public void addWeapon(Weapon weapon);
	public void removeWeapon(Weapon weapon);
	public ArrayList<Weapon> getWeapons();
}
