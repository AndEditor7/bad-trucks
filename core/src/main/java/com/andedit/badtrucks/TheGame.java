package com.andedit.badtrucks;

import com.andedit.badtrucks.handles.Cam3D;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.Util;
import com.andedit.badtrucks.world.World;
import com.andedit.badtrucks.world.WorldEditor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

/** First screen of the application. Displayed after the application is created. */
public class TheGame extends ScreenAdapter 
{
	final Main main;
	
	final World world;
	
	final Camera cam;
	final Cam3D handle;
	
	public TheGame(final Main main) {
		this.main = main;
		cam = new Camera(70f, Util.world.w, Util.world.h);
		cam.near = 2f;
		cam.far  = 1024f;
		cam.position.set(World.LENGTH/2, 24, World.LENGTH/2);
		handle = new Cam3D(cam);
		if (Main.isEditor) {
			world = new WorldEditor();
		} else {
			world = new World(true);
		}
	}
	
	@Override
	public void render(float delta) {
		handle.move(false);
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