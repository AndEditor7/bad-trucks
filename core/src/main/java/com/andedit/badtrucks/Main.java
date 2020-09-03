package com.andedit.badtrucks;

import com.andedit.badtrucks.handles.Inputs;
import com.andedit.badtrucks.handles.Shaders;
import com.andedit.badtrucks.handles.TexLib;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.Game} implementation shared by all platforms. */
public class Main extends Game 
{
	Stage stage;
	Inputs input;
	
	TheMenu manu;
	TheGame game;
	
	AssetManager asset;
	
	@Override
	public void create() {
		Util.world.set(800, 600);
		Util.screen.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new FitViewport(Util.world.w, Util.world.h));
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, input = new Inputs()));
		
		loadAssets();
		OpenGL();
	}

	@Override
	public void resize(int width, int height) {
		Util.screen.set(width, height);
		stage.getViewport().update(width, height);
		super.resize(width, height);
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
	
	private void OpenGL() {
		final GL20 gl = Gdx.gl;
		
        //gl.glClearColor(0.45f, 0.60f, 0.94f, 1);
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		
		gl.glLineWidth(2);
		
		gl.glCullFace(GL20.GL_BACK);
		gl.glEnable(GL20.GL_DEPTH_TEST);
		gl.glDepthFunc(GL20.GL_LEQUAL);
	}
	
	private void loadAssets() {
		final FileHandleResolver resolver = new InternalFileHandleResolver();
		asset = new AssetManager(resolver);
		asset.setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
		asset.setLoader(Texture.class, new TextureLoader(resolver));
		
		TextureParameter texRepeat = new TextureParameter();
		texRepeat.wrapU = TextureWrap.Repeat;
		texRepeat.wrapV = TextureWrap.Repeat;
		texRepeat.magFilter = TextureFilter.Linear;
		texRepeat.minFilter = TextureFilter.Linear;
		asset.load("shaders/terrain.vert", ShaderProgram.class);
		asset.load("textures/grass.png", Texture.class, texRepeat);
	}
	
	private void getAssets() {
		Shaders.getShader(asset);
		TexLib.grass = asset.get("textures/grass.png", Texture.class);
	}
}