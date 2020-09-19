package com.andedit.badtrucks;

import com.andedit.badtrucks.handles.Inputs;
import com.andedit.badtrucks.handles.TexLib;
import com.andedit.badtrucks.mesh.verts.TerrainStream;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.Game} implementation shared by all platforms. */
public class Main extends Game 
{
	/** Are we in the world editor? */
	public static final boolean isEditor = true;
	
	Stage stage;
	
	TheMenu manu;
	TheGame game;
	
	AssetManager asset;
	BitmapFont font;
	
	@Override
	public void create() {		
		Util.world.set(800/2, 600/2);
		Util.screen.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new FitViewport(Util.world.w, Util.world.h));
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new Inputs()));
		
		
		
		loadAssets();
		OpenGL();
	}

	@Override
	public void resize(int width, int height) {
		Util.screen.set(width, height);
		stage.getViewport().update(width, height);
		super.resize(width, height);
		Inputs.clear();
	}
	
	private boolean onLoop = false;
	
	@Override
	public void render() 
	{
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
			return;
		}
		
		Gdx.gl.glUseProgram(0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	
		if (onLoop) {
			super.render();
			Inputs.clearJustPressed();
			return;
		}
		
		if (asset.update()) {
			onLoop = true;
			getAssets();
			setScreen(manu = new TheMenu(this));
		}
		
		// TODO: Render the loading screen.
	}
	
	@Override
	public void dispose() {
		manu.dispose();
		game.dispose();
		asset.dispose();
	}
	
	GLProfiler pro;
	
	private void OpenGL() {
		final GL20 gl = Gdx.gl;
		
        gl.glClearColor(0.42f, 0.56f, 0.90f, 1);
        //gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		
		gl.glLineWidth(2);
		
		gl.glEnable(GL20.GL_CULL_FACE);
		gl.glCullFace(GL20.GL_FRONT);
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glDepthFunc(GL20.GL_LEQUAL);
		
		pro = new GLProfiler(Gdx.graphics);
		pro.enable();
	}
	
	private void loadAssets() {
		final FileHandleResolver resolver = new InternalFileHandleResolver();
		asset = new AssetManager(resolver);
		asset.setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
		asset.setLoader(BitmapFont.class, new BitmapFontLoader(resolver));
		asset.setLoader(Texture.class, new TextureLoader(resolver));
		
		
		final TextureParameter texRepeat = new TextureParameter();
		texRepeat.wrapU = TextureWrap.Repeat;
		texRepeat.wrapV = TextureWrap.Repeat;
		texRepeat.magFilter = TextureFilter.Linear;
		texRepeat.minFilter = TextureFilter.MipMap;
		texRepeat.genMipMaps = true;
		
		asset.load("shaders/terrainStatic.vert", ShaderProgram.class);
		asset.load("shaders/terrainStream.vert", ShaderProgram.class);
		asset.load("textures/grass.png",  Texture.class, texRepeat);
		asset.load("textures/path.png",   Texture.class, texRepeat);
		asset.load("textures/gravel.png", Texture.class, texRepeat);
		asset.load("textures/mixed.png",  Texture.class, texRepeat);
		asset.load("font/Mozart.fnt", BitmapFont.class);
		
	}
	
	private void getAssets() {
		TexLib.grass =  asset.get("textures/grass.png",  Texture.class);
		TexLib.path =   asset.get("textures/path.png",   Texture.class);
		TexLib.gravel = asset.get("textures/gravel.png", Texture.class);
		TexLib.mixed =  asset.get("textures/mixed.png",  Texture.class);
		font = asset.get("font/Mozart.fnt", BitmapFont.class);
		
		//TerrainStatic.ints(asset, "shaders/terrainStatic.vert");
		TerrainStream.ints(asset, "shaders/terrainStream.vert");
	}
}