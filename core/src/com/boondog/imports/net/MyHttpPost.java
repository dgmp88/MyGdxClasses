package com.boondog.imports.net;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;

public class MyHttpPost {
	// A post request. It is its own class. It has variables that change whenever the request is finished.
	public String returnString;
	public boolean done, worked;
	
	public MyHttpPost(String url, String name, String value, HashMap<String,String> parameters) {
		// Set up the post request
		HttpRequest post = new HttpRequest(HttpMethods.POST);
		post.setUrl(url);
		
		// I'm not sure what this header bit is...
		post.setHeader(name, value);
		
		// Set the content
		post.setContent(HttpParametersUtils.convertHttpParameters(parameters));	
		
		Gdx.net.sendHttpRequest(post, new HttpResponseListener() {
			@Override
			public void handleHttpResponse (HttpResponse httpResponse) {
				returnString = httpResponse.getResultAsString();
				worked = true;
				done = true;
			}

			@Override
			public void failed (Throwable t) {
				worked = false;
				done = true;
			}

			@Override
			public void cancelled () {
				worked = false;
				done = true;
			}
		});		

	}
		
}
