package com.zt.pixtok;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zt.pixtok.Screens.Menu;
import com.zt.pixtok.Tools.Values;

public class Pixtok extends Game {

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final int VELOCITY = 80;
	public static final float PPM = 100;

	public static AssetManager manager;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		Values.volumeOn = true;
		manager.load("music/menu_music.ogg", Music.class);
		manager.load("music/level_music.ogg", Music.class);
		manager.load("music/gameover_music.ogg", Music.class);
		manager.load("sounds/game_over.wav", Sound.class);
		manager.load("sounds/coin.wav", Sound.class);
		manager.load("music/victory.ogg", Music.class);
		manager.finishLoading();
		setScreen(new Menu(this));
	}

	@Override
	public void render () {
		super.render();
		manager.update();
	}

	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}
}