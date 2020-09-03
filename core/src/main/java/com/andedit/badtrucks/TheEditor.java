package com.andedit.badtrucks;

import com.andedit.badtrucks.handles.Cam3D;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.Util;
import com.andedit.badtrucks.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

/** First screen of the application. Displayed after the application is created. */
public class TheEditor extends ScreenAdapter 
{
	private final Main main;
	
	final World world;
	
	final Camera cam;
	final Cam3D handle;
	
	public TheEditor(final Main main) {
		this.main = main;
		cam = new Camera(70f, Util.world.w, Util.world.h);
		cam.near = 1f;
		cam.far  = 1024f;
		handle = new Cam3D(cam);
		world = new World();
	}
	
	@Override
	public void render(float delta) {
		handle.move();
		cam.update();
		world.render(cam);
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void dispose() {
		world.dispose();
	}
	
	@Override
	public void show() {
		Gdx.input.setCursorCatched(true);
	}
	
	@Override
	public void hide() {
		Gdx.input.setCursorCatched(false);
	}
}