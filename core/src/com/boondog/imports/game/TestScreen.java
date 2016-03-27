package com.boondog.imports.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.boondog.imports.graphics.MyShapeRenderer;

/*
 * A place to test these classes before installing them. 
 * 
 */

public class TestScreen extends MyScreen {	
	MyShapeRenderer rend;

	// Gradient colors
	Color topColor = new Color(255/255f,0,45/255f,1);
	Color bottomColor = new Color(255/255f,6/255f,143/255f,1);
	Vector2 a = new Vector2(), b = new Vector2(), c = new Vector2();
	
	public TestScreen(MyGame app) {
		super(app);
		rend = new MyShapeRenderer();
		rend.setCamera(MyGame.getViewport().getCamera());
		getAssets().getTexture("badlogic2.jpg");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
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

	
	@Override
	public void reset() {
		changeScreen(new TestScreen(app));
	}
}
