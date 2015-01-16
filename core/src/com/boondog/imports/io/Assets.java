package com.boondog.imports.io;


import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	AssetManager manager = new AssetManager();
	HashMap<String,BitmapFont> fonts = new HashMap<String, BitmapFont>();
	
	private static final String atlasDir = "atlas/";

	public void clearAtlas(String atlas) {
		manager.unload(atlasDir + atlas + ".atlas");
	}
	
	public TextureAtlas getAtlas(String atlas) {
		if (!manager.isLoaded(atlasDir+atlas+".atlas")) {
			manager.load(atlasDir+ atlas + ".atlas",TextureAtlas.class);
			manager.finishLoading();
		}
		return manager.get(atlasDir + atlas + ".atlas",TextureAtlas.class);
	}
	
	public Texture getTexture(String texture) {
		if (!manager.isLoaded(texture)) {
			manager.load(texture,Texture.class);
			manager.finishLoading();
		}
		return manager.get(texture,Texture.class);
	}
	
	public void clearTexture(String texture) {
		manager.unload(texture);
	}
	
	public Skin getSkin(String string) {
		return new Skin(getAtlas(string));
	}
	
	public void loadFont(String fontName, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.genMipMaps = true;
		parameter.minFilter = TextureFilter.MipMapLinearLinear;
		parameter.magFilter = TextureFilter.Linear;
		
		// Make them too big, then minify. This helps for some reason...
		parameter.size = size; // was 135
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		fonts.put(fontName+size, font);
	}
	
	public BitmapFont getFont(String fontName, int size) {
		if (!(fonts.containsKey(fontName+size))) {
			loadFont(fontName,size);
		}
		return fonts.get(fontName+size);
	}
	
	/*
	 *  It seems there is a bug in how sounds are unloaded by the AssetManager.
	 *  
	 *  Just don't unload them, should prevent this.
	 *  see: https://github.com/libgdx/libgdx/issues/1986
	 */
	

	public Sound getSound(String sound) {
		if (!manager.isLoaded(sound)) {
			manager.load(sound, Sound.class);
			manager.finishLoading();
		}
		return manager.get(sound);
	}

	public void unloadAtlas(String string) {
		manager.unload(atlasDir + string + ".atlas");
	}
	
	public AssetManager getManager() {
		return manager;
	}
}
