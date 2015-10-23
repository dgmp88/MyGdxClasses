package com.boondog.imports;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boondog.imports.graphics.MyShapeRenderer;
import com.boondog.imports.graphics.OldSlowMyShapeRenderer;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	MyShapeRenderer rend;
	Texture img;
	Viewport viewport;
	float worldWidth = 720, worldHeight = 1080;
	ShapeRenderer render;
	
	Vector2 a = new Vector2(), b = new Vector2(), c = new Vector2();
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		viewport = new StretchViewport(worldWidth,worldHeight);
		img = new Texture("badlogic.jpg");
		rend = new MyShapeRenderer();
		rend.setCamera(viewport.getCamera());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),true);
		
		a.set(worldWidth/2,worldHeight/2);
	
//		rend.drawCircle(a,20f,20f,20, Color.WHITE);
	//	rend.drawArc(a, 50, 100,0,45, 100, Color.GREEN, Color.BLUE);
//		rend.drawArc(a, 50, 100,0,45, 100, 10f, Color.GREEN);
		rend.begin();

		a.set(100, 10);
		b.set(100, 1000);
		rend.drawLine(a, b, 100, Color.WHITE);
		
		a.set(500, 10);
		b.set(500, 1000);
		rend.drawLine(a, b, 100, Color.WHITE);
		
		
		a.set(100, 1000);
		b.set(1000,100);
		rend.drawLine(a, b, 100, Color.WHITE);
		
		
//		rend.drawLine(a, b, 50, 10, Color.RED);
		rend.end();
		
		
	}
}
