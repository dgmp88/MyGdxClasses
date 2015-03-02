package com.boondog.imports;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Viewport viewport;
	Vector2 pos = new Vector2();
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		viewport = new StretchViewport(720,1080);
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		batch.setProjectionMatrix(viewport.getCamera().combined);
		if (Gdx.input.isTouched()) {
			pos.set(Gdx.input.getX(), Gdx.input.getY());
			viewport.unproject(pos);
			
			batch.begin();
			batch.draw(img, pos.x, pos.y,100,100);
			batch.end();
		}
		

	}
}
