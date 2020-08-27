package com.andedit.badtrucks;

import com.badlogic.gdx.ScreenAdapter;

public class TheMenu extends ScreenAdapter 
{
	private final Main main;

	public TheMenu(final Main main) {
		this.main = main;
	}
	
	@Override
	public void render(float delta) {
		main.setScreen(main.game = new TheGame(main));
	}
}
