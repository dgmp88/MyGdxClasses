package com.boondog.imports.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

public class MyHttpGet {	
	// A get request. It is its own class. It has variables that change whenever the request is finished.
	public String returnString, url;
	public boolean done, worked;

	private long timeOfRequest;
	
	public MyHttpGet(String url) {	
		// New request 
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		timeOfRequest = System.currentTimeMillis();
		this.url = url;
		// Set the URL
		request.setUrl(url);
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse (HttpResponse httpResponse) {
				returnString = httpResponse.getResultAsString();
				done = true;
				worked = true;
			}

			@Override
			public void failed (Throwable t) {
				done = true;
				worked = false;
			}

			@Override
			public void cancelled () {
				done = true;
				worked = false;
			}
		});	
	}
	
	public float getTimeTaken() {
		return (System.currentTimeMillis()-timeOfRequest)/1000f;
	}
}
