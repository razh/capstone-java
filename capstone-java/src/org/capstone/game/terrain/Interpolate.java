package org.capstone.game.terrain;

public class Interpolate {
	public static double linear(double a, double b, double x) {
		return a * (1- x) + b * x;
	}

	public static double cosine(double a, double b, double x) {
		double f = (1.0 - Math.cos(x * Math.PI)) / 2.0;
		return a * (1 - f) + b * f;
	}
}