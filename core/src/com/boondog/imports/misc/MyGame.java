package com.boondog.imports.misc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boondog.imports.io.Assets;

public abstract class MyGame extends Game {
	public static SpriteBatch batch;
	public static Viewport viewport;
	public static Preferences prefs;
	public Assets assets;
	
	public void init() {
		batch = new SpriteBatch();
		prefs = Gdx.app.getPreferences("main");
		assets = new Assets();
		initViewport();
	}
	
	protected abstract void initViewport();
	
	protected void setViewport(float minX, float minY, float maxX, float maxY) {		
		// Here, I should allow small modifications of the world width/height.
		// Now, set up some type of viewport
		viewport = new ExtendViewport(minX,minY,maxX,maxY);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		System.out.println(viewport.getWorldWidth() + "x" + viewport.getWorldHeight());
	}
}
