package com.kierandroid.spacewars;

import com.badlogic.gdx.Game;
import com.kierandroid.spacewars.Screens.GameScreen;

public class EntryPoint extends Game {

    @Override
    public void create() {
		setScreen(new GameScreen(this));
    }

}
