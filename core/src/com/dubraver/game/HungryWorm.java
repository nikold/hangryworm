package com.dubraver.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dubraver.game.screens.MenuScreen;

public class HungryWorm extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create() {
		
		batch = new SpriteBatch();
        font = new BitmapFont();
		setScreen(new MenuScreen(this));
	}
}
