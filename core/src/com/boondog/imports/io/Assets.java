package com.boondog.imports.io;

import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
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
	private static String baseDir = getBaseDir();
	private static String atlasDir = "atlas/";
	private static String fullAtlasDir =  baseDir + atlasDir;
	
	
	private <T> void loadFull (String fileName, Class<T> type) {
		try {
			manager.load(fileName,type);
			manager.finishLoading();
		} catch (Exception e) {
			System.out.println("Most likely, file doesn't exist: ");
			System.out.println(Gdx.files.getLocalStoragePath() + fileName);
			e.printStackTrace();
		}		
	}
	
	private static String getBaseDir() {
		// On desktop, sometimes everything goes wrong unless you use this directory as your head
		// http://stackoverflow.com/questions/12681678/libgdx-how-to-get-a-list-of-files-in-a-directory
		String baseDir = "";
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			baseDir = "./bin/";
		}
		return baseDir;
	}

	public void clearAtlas(String atlas) {
		manager.unload(fullAtlasDir + atlas + ".atlas");
	}
	
	public TextureAtlas getAtlas(String atlas) {
		if (!manager.isLoaded(fullAtlasDir+atlas+".atlas")) {
			loadFull(fullAtlasDir + atlas +".atlas",TextureAtlas.class);
		}
		return manager.get(fullAtlasDir + atlas + ".atlas",TextureAtlas.class);
	}
	
	public Texture getTexture(String texture) {
		if (!manager.isLoaded(texture)) {
			loadFull(baseDir + texture,Texture.class);
		}
		return manager.get(baseDir + texture,Texture.class);
	}
	
	public void clearTexture(String texture) {
		manager.unload(baseDir + texture);
	}
	
	public Skin getSkin(String string) {
		return new Skin(getAtlas(string));
	}
	
	public void loadFont(String fontName, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(baseDir + fontName));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.genMipMaps = true;
		parameter.minFilter = TextureFilter.MipMapLinearNearest;
		parameter.magFilter = TextureFilter.Linear;
		
		// Make them too big, then minify. This helps for some reason...
		parameter.size = size;
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
		if (!manager.isLoaded(baseDir + sound)) {
			manager.load(baseDir + sound, Sound.class);
			manager.finishLoading();
		}
		return manager.get(baseDir + sound);
	}

	public void unloadAtlas(String string) {
		manager.unload(fullAtlasDir + string + ".atlas");
	}
	
	public AssetManager getManager() {
		return manager;
	}
	
	public static void setAtlasDir(String string){
		atlasDir = string;
		fullAtlasDir = baseDir + atlasDir;
	}
	
	public static FileHandle[] listFilesInDir(String dir) {
		return new FileHandle(baseDir + dir).list();
	}
}
