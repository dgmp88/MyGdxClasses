package com.boondog.imports.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boondog.imports.io.Assets;

public abstract class MyScreen extends Group implements Screen  {	
	protected MyGame app;
	
	protected SpriteBatch batch = MyGame.getBatch();
	protected float worldWidth = MyGame.getViewport().getWorldWidth(), worldHeight = MyGame.getViewport().getWorldHeight();
	protected Stage stage = MyGame.getStage();
	protected Assets assets;
	protected Color backgroundColor = new Color(1,1,1,1);
	protected InputMultiplexer inputs;
	
	public MyScreen(MyGame app) {
		this.app = app;
		this.assets = app.getAssets();
		stage.clear();
		stage.addActor(this);
	}
	
	@Override
	public void show() {
		if (inputs!= null) {
			Gdx.input.setInputProcessor(inputs);
		} else {
			Gdx.input.setInputProcessor(stage);		
		}
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
	
	@Override
	public void dispose() {
		clear();
		stage.getRoot().removeActor(this);
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

	public void reset() {} // Used for the debug mode;
	
	public void setDebugMode() {
		inputs = new InputMultiplexer();
		inputs.addProcessor(stage);
		inputs.addProcessor(new DebugInputProcessor(this));
		Gdx.input.setInputProcessor(inputs);
	}
	
	public void addInputProcessor(InputProcessor input) {
		if (inputs == null) {
			inputs = new InputMultiplexer();
			inputs.addProcessor(stage);
		}
		inputs.addProcessor(input);
		Gdx.input.setInputProcessor(inputs);
	}
}
