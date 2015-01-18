package com.boondog.imports.math;

import com.badlogic.gdx.math.Vector2;

public class CircleLogic {
	static Vector2 v = new Vector2();
	static float f;
	
	public static Vector2 findPos(Vector2 center, float radius, float angle, float ellipse) {
		// circular motion: ellipse == 0;
		// straight motion: ellipse appriaching 0 or approaching Inf (Star s1 = new Star(solarSys, new Vector2(camX/2, 10), 180, 0.03f, 2f, 1000f);
		v.x = (float) (radius * Math.cos(Math.toRadians(angle))) * (1+ellipse);
		v.y = (float) (radius * Math.sin(Math.toRadians(angle))) * (1+(ellipse*-1));
		v.add(center);
		return v;
	}
	
	
	public static boolean lineInArc(float arcDir, float arcWidth, float angle) {
		// Uses the solution here: http://gamedev.stackexchange.com/questions/4467/comparing-angles-and-working-out-the-difference
		f = 180 - Math.abs(Math.abs((arcDir % 360) - (angle % 360))-180);
		return (f < arcWidth/2 && f > -arcWidth/2);
	}
}
