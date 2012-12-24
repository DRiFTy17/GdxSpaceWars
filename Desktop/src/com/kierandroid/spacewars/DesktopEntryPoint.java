package com.kierandroid.spacewars;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopEntryPoint {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Space Wars";
        cfg.useGL20 = false;
        cfg.width = 800;
        cfg.height = 480;

        new LwjglApplication(new EntryPoint(), cfg);
    }

}
