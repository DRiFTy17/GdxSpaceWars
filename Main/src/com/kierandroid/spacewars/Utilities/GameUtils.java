package com.kierandroid.spacewars.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameUtils
{
	// This is the "developed for" resolution
	public static final float ASSUME_X = 1280;
	public static final float ASSUME_Y = 720;
	public static float MIN_SPEED = 0.0f;
	public static float MAX_SPEED = 5.0f;
	
	/**
	 * Resizes a sprite based on the "developed for" resolution
	 * 
	 * @return void
	 */
	public static void resize(Sprite sprite)
	{
		float x = Gdx.graphics.getWidth();
		float y = Gdx.graphics.getHeight();
		
		float changeX = x / ASSUME_X;
		float changeY = y / ASSUME_Y;
		
		float change = Math.min(changeX, changeY);
		
		float theWidth = (sprite.getWidth()*change);
		float theHeight = (sprite.getHeight()*change);

		sprite.setSize(theWidth, theHeight);
	}
}
