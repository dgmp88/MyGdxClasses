package com.boondog.imports.math;

import java.util.Arrays;

public class MyMath {
	// Sum
	public static int sum(int[] values) {
		int sum = 0;
		for (int value : values) {
			sum += value;
		}
		return sum;
	}
	
	public static float sum(float[] values) {
		float sum = 0;
		for (float value : values) {
			sum += value;
		}
		return sum;
	}
	
	private static double sum(double[] values) {
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		return sum;
	}
	
	// Minimum value
	public static float min(float[] values) {
		float min = values[0];
		for (float value : values) {
			if (value < min) {
				min = value;
			}
		}
		return min;
	}
	
	public static float max(float[] a) {
		float max = a[0];
		for (int i = 1; i<a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	// Mean 
	public static float mean (float[] d) {
		return sum(d)/d.length;
	}
	
	public static double mean (double[] d) {
		return sum(d)/d.length;
	}
	
	// Find
	public static int[] find (double[] values, double findMe) {
		int[] r = new int[values.length];
		Arrays.fill(r, 0);
		for (int i = 0; i<values.length; i++) {
			if(values[i] == findMe) {
				r[i] = 1;
			}
		}
		return r;
	}
	
	
	// Eqaulity
	public static boolean anyEquals(int[] d, int f) {
		for (int i : d) {
			if (i == f) {
				return true;
			}
		}
		return false;
	}
	
	public static double[] abs(double[] d) {
		double[] d2 = d.clone();
		for (int i = 0; i <d2.length; i++) {
			if (d2[i]<0) {
				d2[i] = -d2[i];
			}
		}
		return d2;
	}

	public static boolean allEquals(int[] squareCol, int j) {
		for (int i = 0; i <squareCol.length;i++) {
			if (j!=squareCol[i]) {
				return false;
			}
		}
		return true;
	}

	public static float[] cumsum(float[] a) {
		float dum = 0;
		float[] csum = new float[a.length];
		for (int i = 0; i< a.length; i++) {
			dum += a[i];
			csum[i] = dum;
		}
		return csum;
	}

	public static float[] arrayAdd(float[] a, float b) {
		for (int i = 0; i<a.length;i++) {
			a[i] += b;
		}
		return a;
	}
	
	public static float[] arrayMult(float[] a, float b) {
		for (int i = 0; i<a.length;i++) {
			a[i] *= b;
		}
		return a;
	}
	
	public static float[] arrayDiv(float[] a, float b) {
		for (int i = 0; i<a.length;i++) {
			a[i] /= b;
		}
		return a;
	}
	
	public static double[] arrayAdd(double[] a, float b) {
		for (int i = 0; i<a.length;i++) {
			a[i] += b;
		}
		return a;
	}
	
	public static double[] arrayMult(double[] a, float b) {
		for (int i = 0; i<a.length;i++) {
			a[i] *= b;
		}
		return a;
	}
	
	public static double[] arrayDiv(double[] a, float b) {
		for (int i = 0; i<a.length;i++) {
			a[i] /= b;
		}
		return a;
	}
	


}
