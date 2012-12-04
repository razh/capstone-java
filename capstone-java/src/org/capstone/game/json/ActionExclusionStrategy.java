package org.capstone.game.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.*;

public class ActionExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		long start = System.nanoTime();
		boolean declaringClassIsAction = Action.class.isAssignableFrom(f.getDeclaringClass());

		if (
			!declaringClassIsAction ||
			(f.getDeclaringClass() == MoveToAction.class && (
				f.getName().equals("x") ||
				f.getName().equals("y")
			)) ||
			(f.getDeclaringClass() == MoveByAction.class && (
				f.getName().equals("amountX") ||
				f.getName().equals("amountY")
			)) ||
			(f.getDeclaringClass() == SizeToAction.class && (
				f.getName().equals("width") ||
				f.getName().equals("height")
			)) ||
			(f.getDeclaringClass() == SizeByAction.class && (
				f.getName().equals("amountWidth") ||
				f.getName().equals("amountHeight")
			)) ||
			(f.getDeclaringClass() == RotateToAction.class) ||
//			&& (
//				f.getName().equals("rotation")
//			)) ||
			(f.getDeclaringClass() == ParallelAction.class && (
				f.getName().equals("actions")
			)) ||
			(f.getDeclaringClass() == TemporalAction.class && (
				f.getName().equals("duration") ||
				f.getName().equals("interpolation")
			))
		) {
			System.out.println(System.nanoTime() - start);
			return false;
		}

		System.out.println(System.nanoTime() - start);
		return declaringClassIsAction;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

}
