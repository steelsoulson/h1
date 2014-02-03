package com.example.androidgames.framework.math;

public class Rectangle4 {
	public final Vector2[] points;
	public final Rectangle bound;
	
	public Rectangle4() {
		points = new Vector2[4];
		points[0] = new Vector2();
		points[1] = new Vector2();
		points[2] = new Vector2();
		points[3] = new Vector2();
		bound = new Rectangle(0, 0, 0, 0);
	}
	
	public void updateBound() {
		float x1 = points[0].x, y1 = points[0].y, x2 = x1, y2 = y1;
		for (int i = 0; i < 4; i++) {
			if (x1 > points[i].x) x1 = points[i].x;
			if (y1 > points[i].y) y1 = points[i].y;
			if (x2 < points[i].x) x2 = points[i].x;
			if (y2 < points[i].y) y2 = points[i].y;
		}
		bound.lowerLeft.set(x1, y1);
		bound.width = x2 - x1;
		bound.height = y2 - y1;
	}
}
