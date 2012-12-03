package org.capstone.game.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;

public class ActionExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
//		if (Action.class.isAssignableFrom(f.getDeclaringClass()) && f.getDeclaringClass().isAssignableFrom(TemporalAction.class))
//				System.out.println("YEEEEEEP");
		if (
			!Action.class.isAssignableFrom(f.getDeclaringClass()) ||
			(f.getDeclaringClass() == MoveByAction.class && (
				f.getName().equals("x") ||
				f.getName().equals("y")
			)) ||
			(f.getDeclaringClass() == SizeByAction.class && (
				f.getName().equals("amountWidth") ||
				f.getName().equals("amountHeight")
			)) ||
			(f.getDeclaringClass() == TemporalAction.class && (
				f.getName().equals("duration") ||
				f.getName().equals("interpolation")
			))
		) {
//			System.out.println("------" + f.getName());

			return false;
//		return Action.class.isAssignableFrom(f.getDeclaringClass());
		}
		return Action.class.isAssignableFrom(f.getDeclaringClass());
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
