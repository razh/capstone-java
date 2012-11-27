package org.capstone.game;

import com.badlogic.gdx.math.Vector2;

public class Geometry {
  public static Vector2 lineLineIntersection(float x0, float y0,
                                             float x1, float y1,
                                             float x2, float y2,
                                             float x3, float y3) {
    float x01 = x0 - x1;
    float x23 = x2 - x3;
    float y01 = y0 - y1;
    float y23 = y2 - y3;

    float c = x01 * y23 - y01 * x23;
    if (Math.abs(c) < State.EPSILON) {
      return null;
    } else {
      float a = x0 * y1 - y0 * x1;
      float b = x2 * y3 - y2 * x3;

      float px = (a * x23 - b * x01) / c;
      float py = (a * y23 - b * y01) / c;

      return new Vector2(px, py);
    }
  }

  // (x, y) is the point we are examining.
  public static boolean obbContains(float x, float y, float cx, float cy, float width, float height, float rotation) {
    x -= cx;
    y -= cy;

    if (rotation != 0.0f) {
      float cos = (float) Math.cos(rotation);
      float sin = (float) Math.sin(rotation);

      // Rotated coordinates.
      float rX =  cos * x + sin * y;
      float rY = -sin * x + cos * y;

      x = rX;
      y = rY;
    }

    if (Math.abs(x) <= width && Math.abs(y) <= height)
      return true;

    return false;
  }
}
