package com.boondog.imports.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 * This chap draws a repeating sprite, allows you to set some trim, and handles the ends 
 * vaguely intelligently.
 * 
 * I feel like I must have reinvented the wheel here, but hey hey.
 * 
 * @author george
 *
 */


public class RepeatingSprite {
	TextureAtlas atlas;
	String spriteName;
	Vector2 pos, size;
	boolean horizontal;
	float trimPixels;
	
	Sprite sprite;
	int wholePieces;
	float spriteWidth, spriteHeight;
	float adjustedWidth, adjustedHeight;
	float trim;
	
	Vector2 leftOverPos, leftOverSize;
	TextureRegion leftOver;

	
	float tmpX, tmpY;
	
	Color color;
	
	public RepeatingSprite(TextureAtlas skin, String spriteName, boolean horizontal, Vector2 pos, Vector2 size, float trimPixels) {
		 this.atlas = skin;
		 this.spriteName = spriteName;
		 this.horizontal = horizontal;
		 this.pos = pos;
		 this.size = size;
		 this.trimPixels = trimPixels;
		 init();
	}

	private void init() {
		sprite = atlas.createSprite(spriteName);
		int pixelWidth = sprite.getRegionWidth(), pixelHeight = sprite.getRegionHeight();
		leftOverPos = new Vector2();
		leftOverSize = new Vector2(10,10);
		if (horizontal) {
			// Figure out our scaling.
			float scale = size.y/pixelHeight;
			spriteWidth = sprite.getWidth() * scale;
			spriteHeight= sprite.getHeight() * scale;
			trim = trimPixels * scale;
			
			// How wide is the usable bit after removing trim?
			adjustedWidth = spriteWidth - trim;
			
			// How many whole sprites do we have?
			wholePieces = (int) Math.floor(size.x/(adjustedWidth));
			
			float wholePiecesWidth = wholePieces*adjustedWidth;
			leftOverSize = new Vector2(size.x - wholePiecesWidth, size.y);

			// If the trim is big, the end of the sprite can overhang, so just use that sprite instead.
			if (trim > leftOverSize.x) {
				wholePieces--;
				wholePiecesWidth = wholePieces*adjustedWidth;
				leftOverPos = new Vector2(wholePiecesWidth + pos.x,pos.y);
				leftOverSize = new Vector2(size.x - wholePiecesWidth, size.y);
				
				AtlasRegion tmp = atlas.findRegion(spriteName);
				int w = (int) (leftOverSize.x / scale);
				leftOver = new TextureRegion(tmp,0,0,w,pixelHeight);
				
			} else {
				leftOverPos = new Vector2(wholePiecesWidth + pos.x,pos.y);
				AtlasRegion tmp = atlas.findRegion(spriteName);
				int w = (int) (leftOverSize.x / scale);
				leftOver = new TextureRegion(tmp,0,0,w,pixelHeight);
			}
		} else {
			// Figure out our scaling.
			float scale = size.x/pixelWidth;
			spriteWidth = sprite.getWidth() * scale;
			spriteHeight= sprite.getHeight() * scale;
			trim = trimPixels * scale;
			
			// How wide is the usable bit after removing trim?
			adjustedHeight = spriteHeight - trim;
			
			// How many whole sprites do we have?
			wholePieces = (int) Math.floor(size.y/(adjustedHeight));
			
			float wholePiecesHeight = wholePieces*adjustedHeight;
			leftOverSize = new Vector2(size.x,size.y - wholePiecesHeight);

			// If the trim is big, the end of the sprite can overhang, so just use that sprite instead.
			if (trim > leftOverSize.y) {
				wholePieces--;
				wholePiecesHeight = wholePieces*adjustedHeight;
				leftOverPos = new Vector2(pos.x,wholePiecesHeight + pos.y);
				leftOverSize = new Vector2(size.x, size.y - wholePiecesHeight);
				
				AtlasRegion tmp = atlas.findRegion(spriteName);
				int h = (int) (leftOverSize.y / scale);
				leftOver = new TextureRegion(tmp,0,0,pixelWidth,h);
				
			} else {
				leftOverPos = new Vector2(pos.x,wholePiecesHeight + pos.y);
				AtlasRegion tmp = atlas.findRegion(spriteName);
				int h = (int) (leftOverSize.y / scale);
				leftOver = new TextureRegion(tmp,0,0,pixelWidth,h);
			}
		}
		sprite.setSize(spriteWidth, spriteHeight);

	}
		
	public void drawBatch(SpriteBatch batch) {
		if (horizontal) {
			tmpX = pos.x;
			for (int i = 0; i < wholePieces; i ++) {
				tmpX = pos.x + (spriteWidth * i) - (trim * i);
				sprite.setBounds(tmpX,pos.y, spriteWidth, spriteHeight);
				if (color != null) {
					sprite.setColor(color);
				}
				sprite.draw(batch);
			}			
			batch.draw(leftOver, leftOverPos.x, leftOverPos.y, leftOverSize.x, leftOverSize.y);
			
		} else {
			tmpY = pos.y;
			for (int i = 0; i < wholePieces; i ++) {
				tmpY = pos.y + (spriteHeight * i) - (trim * i);
				sprite.setBounds(pos.x,tmpY, spriteWidth, spriteHeight);
				if (color != null) {
					sprite.setColor(color);
				}
				sprite.draw(batch);
			}			
			batch.draw(leftOver, leftOverPos.x, leftOverPos.y, leftOverSize.x, leftOverSize.y);
			
		}
	}
	
	
	public void drawDebug(ShapeRenderer renderer) {
		renderer.set(ShapeType.Line);
		renderer.setColor(Color.BLACK);
		renderer.rect(leftOverPos.x, leftOverPos.y, leftOverSize.x, leftOverSize.y);
	}

	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
