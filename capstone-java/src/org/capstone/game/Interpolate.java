package org.capstone.game;

public class Interpolate {
	public static float linear(float t, float b, float c, float d) {
		return c * t / d + b;
	}

	public static float easeInQuad(float t, float b, float c, float d) {
		t /= d;
		return c * t * t + b;
	}

	private static float easeOutQuad(float t, float b, float c, float d) {
		t /= d;
		return -c * t * (t - 2) + b;
	}

	private static float easeInOutQuad(float t, float b, float c, float d) {
		t /= d / 2;
		if (t < 1)
			return c / 2 * t * t + b;
		t--;
		return -c / 2 * (t * (t - 2) - 1) + b;
	}

	private static float easeInCubic(float t, float b, float c, float d) {
		t /= d;
		return c * t * t * t + b;
	}

	private static float easeOutCubic(float t, float b, float c, float d) {
		t /= d;
		t--;
		return c * (t * t * t + 1) + b;
	}

	private static float easeInOutCubic(float t, float b, float c, float d) {
		t /= d / 2;
		if (t < 1)
			return c / 2 * t * t * t + b;
		t -= 2;
		return c / 2 * (t * t * t + 2) + b;
	}

	private static float easeInQuart(float t, float b, float c, float d) {
		t /= d;
		return c * t * t * t * t + b;
	}

	private static float easeOutQuart(float t, float b, float c, float d) {
		t /= d;
		t--;
		return -c * (t * t * t * t - 1) + b;
	}

	private static float easeInOutQuart(float t, float b, float c, float d) {
		t /= d / 2;
		if (t < 1)
			return c / 2 * t * t * t * t + b;
		t -= 2;
		return -c / 2 * (t * t * t * t - 2) + b;
	}

	private static float easeInQuint(float t, float b, float c, float d) {
		t /= d;
		return c * t * t * t * t * t + b;
	}

	private static float easeOutQuint(float t, float b, float c, float d) {
		t /= d;
		t--;
		return c * (t * t * t * t * t + 1) + b;
	}

	private static float easeInOutQuint(float t, float b, float c, float d) {
		t /= d / 2;
		if (t < 1)
			return c / 2 * t * t * t * t * t + b;
		t -= 2;
		return c / 2 * (t * t * t * t * t + 2) + b;
	}

	private static float easeInSine(float t, float b, float c, float d) {
		return (float) (-c * Math.cos(t / d * (Math.PI / 2)) + c + b);
	}

	private static float easeOutSine(float t, float b, float c, float d) {
		return (float) (c * Math.sin(t / d * (Math.PI / 2)) + b);
	}

	private static float easeInOutSine(float t, float b, float c, float d) {
		return (float) (-c / 2 * (Math.cos(Math.PI * t / d) - 1) + b);
	}

	private static float easeInExpo(float t, float b, float c, float d) {
		return (float) (c * Math.pow(2, 10 * (t / d - 1)) + b);
	}

	private static float easeOutExpo(float t, float b, float c, float d) {
		return (float) (c * (-Math.pow(2, -10 * t/ d) + 1) + b);
	}

	private static float easeInOutExpo(float t, float b, float c, float d) {
		t /= d / 2;
		if (t < 1)
			return (float) (c / 2 * Math.pow(2, 10 * (t - 1)) + b);
		t--;
		return (float) (c / 2 * (- Math.pow(2, -10 * t) + 2) + b);
	}

	private static float easeInCirc(float t, float b, float c, float d) {
		t /= d;
		return (float) (-c * (Math.sqrt(1 - t * t) - 1) + b);
	}

	private static float easeOutCirc(float t, float b, float c, float d) {
		t /= d;
		t--;
		return (float) (c * Math.sqrt(1 - t * t) + b);
	}

	private static float easeInOutCirc(float t, float b, float c, float d) {
		t /= d / 2;
		if (t < 1)
			return (float) (-c / 2 * (Math.sqrt(1 - t * t) - 1) + b);
		t -= 2;
		return (float) (c / 2 * (Math.sqrt(1 - t * t) + 1) + b);
	}
}
