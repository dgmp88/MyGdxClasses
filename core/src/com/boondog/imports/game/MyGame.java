package com.boondog.imports.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boondog.imports.io.Assets;

public abstract class MyGame extends Game {
	private static SpriteBatch batch;
	private static Viewport viewport;
	private static Preferences prefs;
	private static Stage stage;
	private Assets assets;
	
	public void init() {
		batch = new SpriteBatch();
		prefs = Gdx.app.getPreferences("main");
		assets = new Assets();
		initViewport();
		stage = new Stage(viewport,batch);
	}
	
	protected abstract void initViewport();
	
	protected static void setExtendViewport(float minX, float minY, float maxX, float maxY) {		
		// Here, I should allow small modifications of the world width/height.
		// Now, set up some type of viewport
		viewport = new ExtendViewport(minX,minY,maxX,maxY);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
	}
	
	protected static void setViewport(Viewport viewport) {		
		MyGame.viewport = viewport;
	}
	
	public static void printViewportDims() {
		System.out.println(viewport.getWorldWidth() + "x" + viewport.getWorldHeight());
	}
	
	public void changeScreen(Screen screen){
		Screen oldScreen = getScreen();
		setScreen(screen);
		oldScreen.dispose();
		oldScreen = null;
	}

	public static SpriteBatch getBatch() {
		batch.setProjectionMatrix(viewport.getCamera().combined);
		return batch;
	}

	public static Viewport getViewport() {
		return viewport;
	}

	public static Preferences getPrefs() {
		return prefs;
	}

	public Assets getAssets() {
		return assets;
	}

	public static Matrix4 getProjMat() {
		return getViewport().getCamera().combined;
	}

	public static Stage getStage() {
		return stage;
	}
}
