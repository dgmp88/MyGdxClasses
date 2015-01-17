package com.boondog.imports.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boondog.imports.io.Assets;

public abstract class MyScreen implements Screen {	
	protected MyGame app;
	
	protected SpriteBatch batch = MyGame.getBatch();
	protected float worldWidth = MyGame.getViewport().getWorldWidth(), worldHeight = MyGame.getViewport().getWorldHeight();
	protected Stage stage = new Stage(MyGame.getViewport(), MyGame.getBatch());
	protected Assets assets;
	protected Color backgroundColor = new Color(1,1,1,1);
	
	public MyScreen(MyGame app) {
		this.app = app;
		this.assets = app.getAssets();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);		
	}
	
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void resize(int width, int height) {
		MyGame.getViewport().update(width, height);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}
	
	public void clearColor() {
		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public Assets getAssets() {
		return assets;
	}
	
	public float getWorldWidth() {
		return MyGame.getViewport().getWorldWidth();
	}
	
	
	public float getWorldHeight() {
		return MyGame.getViewport().getWorldHeight();
	}
	
	public static Viewport getViewport() {
		return MyGame.getViewport();
	}
	
	public static Matrix4 getProjMatrix() {
		return getViewport().getCamera().combined;
	}
	
	public void changeScreen(MyScreen newScreen) {
		app.changeScreen(newScreen);
	}
	
	public MyGame getApp() {
		return app;
	}
}
