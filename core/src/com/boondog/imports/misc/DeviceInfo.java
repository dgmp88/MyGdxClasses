package com.boondog.imports.misc;

import java.util.TimeZone;

import com.badlogic.gdx.Gdx;

public class DeviceInfo {
	public static int getTZ() {
		return msToHours(TimeZone.getDefault().getRawOffset());
	}
	
	public static int getDST() {
		return msToHours(TimeZone.getDefault().getDSTSavings());
	}
	
	
	public static String getTZT(){
		String timeZoneText = TimeZone.getDefault().getID();
		timeZoneText = timeZoneText.replace("/", "_");
		timeZoneText = timeZoneText.replace("\\", "_");
		return timeZoneText;
	}
	
	public static String getPlatform() {
		return Gdx.app.getType().toString();
	}

	public static float getPpiX() {
		return Gdx.graphics.getPpiX();
	}
	
	public static float getPpiY() {
		return Gdx.graphics.getPpiY();
	}
	
	public static int getSoftwareVersion() {
		return Gdx.app.getVersion();
	}
		
	private static int msToHours(int t) {
		t /= 1000; // To seconds
		t /= 60; // To minutes
		t /= 60; // To hours
		return t;
	}

	public static int getWidth() {
		return Gdx.graphics.getWidth();
	}
	
	public static int getHeight() {
		return Gdx.graphics.getHeight();
	}
}
