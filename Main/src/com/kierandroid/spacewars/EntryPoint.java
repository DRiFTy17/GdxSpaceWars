package com.kierandroid.spacewars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.kierandroid.spacewars.Screens.LoadingScreen;

public class EntryPoint extends Game {

	public AssetManager assets;

    @Override
    public void create() {
		assets = new AssetManager();

		setScreen(new LoadingScreen(this));
		//setScreen(new GameScreen(this));
    }

}
