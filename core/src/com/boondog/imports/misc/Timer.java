package com.boondog.imports.misc;

import java.util.HashMap;


public class Timer {
	// The data types these return are important. 
	// http://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
	
	// int = 32 bit integer.
	// long = 64 bit integer
	// float = 32 bit float
	// double = 64 bit float
	
	int nTimers;
	long timers[];
	HashMap<String, Long> timersMap;
	
	public Timer() {
		init(5);
	}
	
	public void namedTimers(String[] names) {
		timersMap = new HashMap<String, Long>();
		for (String s : names) {
			timersMap.put(s, System.nanoTime());
		}
	}
	
	
	public Timer(int i) {
		init (i);
	}
	
	private void init (int nTimers) {
		this.nTimers = nTimers;
		timers = new long[nTimers];
		for (int i = 0; i<nTimers; i++) {
			reset(i);
		}
	}
	
	public long getNanoTime(int i) {
		return System.nanoTime()-timers[i];
	}
	
	public long getNanoTime(String s) {
		return System.nanoTime()-timersMap.get(s);
	}
	
	public double getMilliTime(int i) {
		return getNanoTime(i)/1000000d;
	}
	
	public double getMilliTime(String s) {
		return getNanoTime(s)/1000000d;
	}
	
	public double getSecTime(int i) {
		return getMilliTime(i)/1000d;
	}
	
	public double getSecTime(String s) {
		return getMilliTime(s)/1000d;
	}
	
	public void reset(int i) {
		timers[i] = System.nanoTime();
	}
	
	public void reset(String s) {
		// Java docs says old item is replaced, so this shouldn't lead to duplicates
		timersMap.put(s, System.nanoTime());
	}

}
