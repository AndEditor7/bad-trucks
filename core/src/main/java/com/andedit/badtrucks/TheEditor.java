package com.andedit.badtrucks;

import com.andedit.badtrucks.handles.Inputs;
import com.andedit.badtrucks.utils.Util;
import com.andedit.badtrucks.world.WorldEditor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.viewport.Viewport;

/** The world editor screen extended from TheGame screen. */
public class TheEditor extends TheGame 
{
	/** Are we rotate the camera? */
	boolean isFocus = true;
	final Viewport view;
	
	boolean mode = true;
	int index = 1;
	
	ImmediateModeRenderer20 IMR;
	
	WorldEditor world;
	
	public TheEditor(final Main main) {
		super(main);
		view = main.stage.getViewport();
		IMR = new ImmediateModeRenderer20(500, false, true, 0);
		this.world = (WorldEditor)super.world;
	}
	
	private float lastHeight = Float.NaN;
	
	private void update() {
		if (Inputs.isKeyJustPressed(Keys.TAB)) {
			isFocus = !isFocus;
			if (isFocus) {
				Gdx.input.setCursorCatched(true);
				Inputs.resetMouse();
			} else {
				Gdx.input.setCursorCatched(false);
				Gdx.input.setCursorPosition(Util.screen.w>>1, Util.screen.h>>1);
			}
		}
		
		if (Inputs.isKeyPressed(Keys.NUM_1)) {
			index = -1;
		} else if (Inputs.isKeyPressed(Keys.NUM_2)) {
			index = 0;
		} else if (Inputs.isKeyPressed(Keys.NUM_3)) {
			index = 1;
		}
		
		if (Inputs.isKeyJustPressed(Keys.F1)) {
			mode = !mode;
		}
		
		if (!isFocus) {
			boolean up   = Gdx.input.isButtonPressed(Buttons.LEFT);
			boolean down = Gdx.input.isButtonPressed(Buttons.RIGHT);
			final Ray ray = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
			final Vector3 pos = world.raycast(cam.position, ray.direction, lastHeight);
			
			if (mode) {
				if (up || down) {
					if (Float.isNaN(lastHeight)) lastHeight = pos.y;
					if (pos != null) {
						world.bumpHeights(pos.x, pos.z, 4, 16f, up ? 0.2f : (down ? -0.2f : 0f));
					}
				} else {
					lastHeight = Float.NaN;
				}
			} else {
				if (up || down) {
					if (pos != null) {
						world.paint(pos.x, pos.z, 4, 16, 1.0f, index); // 8, 32
					}
				}
			}
		}
	}
	
	@Override
	public void render(float delta) 
	{
		update();
		if (isFocus) handle.move(isFocus);
		cam.update();
		world.render(cam);
		
		final BitmapFont font = main.font;
		final Batch batch = main.stage.getBatch();
		batch.setProjectionMatrix(main.stage.getViewport().getCamera().combined);
		batch.begin();
		font.draw(batch, "Index: " + index, 6, 21);
		font.draw(batch, "Mode: " + (mode ? "terrain" : "paint"), 6, 10);
		batch.end();
	}
	
	@Override
	public void show() {
		if (isFocus) {
			Gdx.input.setCursorCatched(true);
		}
	}
	
	@Override
	public void hide() {
		Gdx.input.setCursorCatched(false);
		main.stage.clear();
	}
	
	@Override
	public void dispose() {
		IMR.dispose();
	}
}