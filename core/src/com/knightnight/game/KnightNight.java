package com.knightnight.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.knightnight.game.Screens.PlayScreen;
import com.knightnight.game.Screens.TitleScreen;

public class KnightNight extends Game {

	public static final String TITLE = "Knight Night";

	public static int WIDTH;
	public static int HEIGHT;
	//public static final int WIDTH = 480;
	//public static final int HEIGHT = 800;

	public SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
