package org.capstone.game.entities;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.MeshStage;
import org.capstone.game.entities.weapons.LaserGun;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class EntityExclusionStrategy implements ExclusionStrategy {

	public EntityExclusionStrategy() {
	}

	@Override
	public boolean shouldSkipClass(Class<?> c) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		// White-list.
		if (
//			(f.getDeclaringClass() == MeshStage.class && (
//				// f.getName().equals("meshRoot")
//				f.getName().equals("entities")    ||
//				f.getName().equals("projectiles") ||
//				f.getName().equals("text")        ||
//				f.getName().equals("tests")       ||
//				f.getName().equals("shaderProgram")
//			)) ||
			// (f.getDeclaringClass() == Group.class && (
			// 	f.getName().equals("children")
			// )) ||
			 (f.getDeclaringClass() == MeshGroup.class && (
			 	f.getName().equals("children")
			 ))
//			(f.getDeclaringClass() == Array.class && (
//				f.getName().equals("items")
//			))
		)
			return false;

		// Black-list.
		return (
			f.getDeclaringClass() == Stage.class ||
					f.getDeclaringClass() == MeshStage.class ||
			// f.getDeclaringClass() == Group.class ||
			// f.getDeclaringClass() == MeshGroup.class ||

			(f.getDeclaringClass() == Stage.class && (
			// 	!f.getName().equals("entities")    &&
			// 	!f.getName().equals("projectiles") &&
			// 	!f.getName().equals("text")        &&
			// 	!f.getName().equals("tests")       &&
			// 	!f.getName().equals("shaderProgram")
				f.getName().equals("root")
			// // 	f.getName().equals("pointerOverActors")
			)) ||
			// (f.getDeclaringClass() == MeshGroup.class) ||
			(f.getDeclaringClass() == Actor.class && (
				f.getName().equals("stage")   ||
				f.getName().equals("parent")  ||
				f.getName().equals("originX") ||
				f.getName().equals("originY") ||
				f.getName().equals("scaleX")  ||
				f.getName().equals("scaleY")
			)) ||
			(f.getDeclaringClass() == Weapon.class && (
				f.getName().equals("actor")   ||
				f.getName().equals("targetX") ||
				f.getName().equals("targetY")
			)) ||
			(f.getDeclaringClass() == LaserGun.class && (
				f.getName().equals("laserBeam")
			)) ||
			(f.getDeclaringClass() == MeshActor.class && (
				// f.getName().equals("entity") ||
				f.getName().equals("shaderProgram")
			)) ||
			(f.getDeclaringClass() == MeshGroup.class && (
				f.getName().equals("stage")  ||
				// f.getName().equals("meshStage")  ||
				f.getName().equals("entity") ||
				f.getName().equals("shaderProgram")
			)) ||
			(f.getDeclaringClass() == Entity.class && (
				f.getName().equals("actor")
			)) ||
			(f.getDeclaringClass() == EntityGroup.class && (
				f.getName().equals("actor") ||
				f.getName().equals("segments") ||
				f.getName().equals("segmentGroup")
			)) ||
			(f.getDeclaringClass() == SnapshotArray.class && (
				f.getName().equals("snapshot") ||
				f.getName().equals("recycled") ||
				f.getName().equals("snapshots")
			))
		);
	}
}
