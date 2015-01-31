package com.boondog.imports.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MyVectors {
	static Vector2 tmp = new Vector2(), tmp1 = new Vector2(), tmp2 = new Vector2();
	static float min, max, sub, minX, minY, maxX, maxY;
		
	/*
	 * 
	 * Center the data around f on X/Y (true/false);
	 * 
	 */
	
	public static void center(Array<Vector2> vecs, float f, boolean x) {
		if (x) {
			// Subtract out the minimum (smallest value to 0)
			sub = getMin(vecs, true);
			for (Vector2 s : vecs) {
				s.x -= sub;
			}
			
			// Subtract out half the max value (center to 0)
			sub = getMax(vecs, true)/2;
			for (Vector2 s : vecs) {
				s.x -= sub;
			}
			
			// Add 0.5 (center to 0.5)
			for (Vector2 s : vecs) {
				s.x += f;
			}	
		} else{
			// Subtract out the minimum (smallest value to 0)
			sub = getMin(vecs, false);
			for (Vector2 s : vecs) {
				s.y -= sub;
			}
			
			// Subtract out half the max value (center to 0)
			sub = getMax(vecs, false)/2;
			for (Vector2 s : vecs) {
				s.y -= sub;
			}
			
			// Add 0.5 (center to 0.5)
			for (Vector2 s : vecs) {
				s.y += f;
			}
		}
	}
	
	/*
	 * Scale to minX, minY, maxX, maxY
	 */
	public static void scaleTo(Array<Vector2> vecs, float minX, float minY, float maxX, float maxY) {
		norm(vecs);
		for (Vector2 s : vecs) {
			s.x = s.x * (maxX - minX);
			s.y = s.y * (maxY - minY);
			s.add(minX,minY);
		}
	}
	
	/*
	 * 
	 * minX = 0, minY = 0, maxX = 1, maxY = 1
	 * 
	 */
	public static void norm(Array<Vector2> vecs) {
		minX = getMin(vecs,true);
		maxX = getMax(vecs,true);
		minY = getMin(vecs,false);
		maxY = getMax(vecs,false);
		
		for (Vector2 s : vecs) {
			s.x = (s.x - minX)/(maxX-minX);
			s.y = (s.y - minY)/(maxY-minY);
		}
	}

	/*
	 * Get smallest value on X/Y
	 */
	public static float getMin(Array<Vector2> vecs, boolean x) {
		if (x) {
			min = vecs.first().x;
		} else {
			min = vecs.first().y;
		}
		for (Vector2 v : vecs) {
			if (x) {
				min = Math.min(min, v.x);
			} else {
				min = Math.min(min, v.y);
			}
		}
		return min;
	}
	
	/*
	 * Get maximum value on X/Y
	 */
	public static float getMax(Array<Vector2> vecs, boolean x) {
		if (x) {
			max = vecs.first().x;
		} else {
			max = vecs.first().y;
		}
		for (Vector2 v : vecs) {
			if (x) {
				max = Math.max(max, v.x);
			} else {
				max = Math.max(max, v.y);
			}
		}
		return max;
	}
	
	public static void rotate(Array<Vector2> vecs, Vector2 rotateAbout, float deg) {
		for (Vector2 v : vecs) {
			v = rotateAbout(v,rotateAbout,deg);
		}
	}
	
	private static Vector2 rotateAbout(Vector2 toRotate, Vector2 rotateAbout, float degrees) {
		toRotate.sub(rotateAbout);
		toRotate.rotate(degrees);
		toRotate.add(rotateAbout);
		return toRotate;
	}
}
