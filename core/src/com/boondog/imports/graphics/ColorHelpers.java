package com.boondog.imports.graphics;

import com.badlogic.gdx.graphics.Color;

public class ColorHelpers {
	static Color color = new Color(); // Reuse this guy a lot.
	
	/*
	 * 
	 * Supply h, s, v each as floats 0 < 1. 
	 * 
	 * Color object is reused, copy it!
	 * 
	 */
	public static Color HSBtoRGB(float h, float s, float v) {
		int x = java.awt.Color.HSBtoRGB(h,s,v);		
		color.set(x);
		return color;
	}
	
}
