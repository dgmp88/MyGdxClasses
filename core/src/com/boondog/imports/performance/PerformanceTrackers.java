package com.boondog.imports.performance;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.PerformanceCounters;

/** 
 * 
 * Some simple performance tracking
 * 
 * @author george
 *
 */

public class PerformanceTrackers {
	public static PerformanceCounters getCounters(int n) {
		PerformanceCounters counters = new PerformanceCounters();
		 for (int i = 0; i < n; i++) {
			 counters.add(String.valueOf(i));
		 }
		 return counters;
	}
	
	public static void printBasicPerformanceStats(PerformanceCounters counters) {
		for (PerformanceCounter c : counters.counters) {
			System.out.println(c.name + ": Avg: " + c.time.average * 1000 + " max " + c.time.max *1000 + " total " +c.time.total*1000);
		}
	}
	
	public static void resetCounters(PerformanceCounters counters) {
		for (PerformanceCounter c : counters.counters) {
			c.reset();
		}
	}
	
	
	/**
	 * Copy Pasta from http://stackoverflow.com/questions/466878/can-you-get-basic-gc-stats-in-java
	 */
	public static void printGCStats() {
	    long totalGarbageCollections = 0;
	    long garbageCollectionTime = 0;
	    
	    
	    if (Gdx.app.getType() == ApplicationType.iOS) {
	    	System.out.println("GC stats crashes on iOS.");
	    	return;
	    }

	    for(GarbageCollectorMXBean gc :
	            ManagementFactory.getGarbageCollectorMXBeans()) {

	        long count = gc.getCollectionCount();

	        if(count >= 0) {
	            totalGarbageCollections += count;
	        }

	        long time = gc.getCollectionTime();

	        if(time >= 0) {
	            garbageCollectionTime += time;
	        }
	    }

	    System.out.println("Total Garbage Collections: "
	        + totalGarbageCollections);
	    System.out.println("Total Garbage Collection Time (ms): "
	        + garbageCollectionTime);
	}
}
