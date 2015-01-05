package com.boondog.imports.misc;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public AssetManager manager = new AssetManager();
	BitmapFont font;
	
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
	
	public Skin getSkin(String string) {
		return new Skin(getAtlas(string));
	}
	
	public void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Orbitron-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.genMipMaps = true;
		parameter.minFilter = TextureFilter.MipMapLinearLinear;
		parameter.magFilter = TextureFilter.Linear;
		
		// Make them too big, then minify. This helps for some reason...
		parameter.size = 70; // was 135
		font = generator.generateFont(parameter);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
	}
	
	public BitmapFont getFont() {
		return font;
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
	
}
