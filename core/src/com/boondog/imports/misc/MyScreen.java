package com.boondog.imports.misc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class MyScreen implements Screen {	
	public Game app;

	protected SpriteBatch batch = MyGame.batch;
	protected float worldWidth = MyGame.viewport.getWorldWidth(), worldHeight = MyGame.viewport.getWorldHeight();
	public Stage stage = new Stage(MyGame.viewport, MyGame.batch);
	protected Assets assets;
	
	public MyScreen(MyGame app) {
		this.app = app;
		this.assets = app.assets;
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
		MyGame.viewport.update(width, height);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
}
