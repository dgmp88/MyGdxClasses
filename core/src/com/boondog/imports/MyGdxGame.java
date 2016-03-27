package com.boondog.imports;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.boondog.imports.game.MyGame;
import com.boondog.imports.game.TestScreen;

public class MyGdxGame extends MyGame {
	float worldWidth = 720, worldHeight = 1080;

	
	@Override
	public void create () {
		init(); // MyGame method for the grapichs stuff. Sets up the viewport.]
		setScreen(new TestScreen(this));
	}
	

	@Override
	protected void initViewport() {
		setViewport(new StretchViewport(worldWidth,worldHeight));
	}
}
