package org.capstone.game.json;

import org.capstone.game.MeshActor;
import org.capstone.game.MeshGroup;
import org.capstone.game.MeshStage;
import org.capstone.game.PolygonMeshActor;
import org.capstone.game.TextMeshActor;
import org.capstone.game.TextMeshGroup;
import org.capstone.game.entities.Entity;
import org.capstone.game.entities.EntityGroup;
import org.capstone.game.entities.weapons.LaserGun;
import org.capstone.game.entities.weapons.Weapon;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class MeshStageExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> c) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		// White-list.
		if (
			(f.getDeclaringClass() == MeshStage.class && (
				f.getName().equals("entities")    ||
				f.getName().equals("projectiles") ||
				f.getName().equals("text")
			)) ||
			(f.getDeclaringClass() == Group.class && (
				f.getName().equals("children")
			))
		)
			return false;

		// Black-list.
		if (
			f.getDeclaringClass() == Stage.class ||
			f.getDeclaringClass() == MeshStage.class ||
			f.getDeclaringClass() == Group.class ||
			f.getDeclaringClass() == Mesh.class ||

			(f.getDeclaringClass() == Actor.class && (
				f.getName().equals("stage")   ||
				f.getName().equals("parent")  ||
				f.getName().equals("originX") ||
				f.getName().equals("originY") ||
				f.getName().equals("scaleX")  ||
				f.getName().equals("scaleY")
			)) ||
			(f.getDeclaringClass() == MeshActor.class && (
					f.getName().equals("shaderProgram")
				)) ||
			(f.getDeclaringClass() == PolygonMeshActor.class && (
				f.getName().equals("mesh")
			)) ||
			(f.getDeclaringClass() == TextMeshActor.class && (
				f.getName().equals("mesh") ||
				f.getName().equals("indices")
			)) ||
			(f.getDeclaringClass() == TextMeshGroup.class && (
				f.getName().equals("textActors")
			)) ||
			(f.getDeclaringClass() == Weapon.class && (
				f.getName().equals("actor")   ||
				f.getName().equals("targetX") ||
				f.getName().equals("targetY")
			)) ||
			(f.getDeclaringClass() == LaserGun.class && (
				f.getName().equals("laserBeam")
			)) ||
			(f.getDeclaringClass() == MeshGroup.class && (
				f.getName().equals("stage")  ||
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
			(f.getDeclaringClass() == Action.class && (
				f.getName().equals("actor") ||
				f.getName().equals("pool")
			))
		) {
			return true;
		} else {
			return false;
		}
	}
}
