package com.kierandroid.spacewars.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kierandroid.spacewars.Enumerations.ControlsConfiguration;
import com.kierandroid.spacewars.Enumerations.State;
import com.kierandroid.spacewars.Utilities.GameState;
import com.kierandroid.spacewars.Utilities.GameUtils;

public class FireButton extends Actor
{
	private Sprite buttonNormal;
	private Sprite buttonPressed;
	private Rectangle boundingBox;

	public boolean isTouched = false;

	private float positionX;
	private float positionY;

	public FireButton(ControlsConfiguration configuration)
	{
		this.setTouchable(Touchable.enabled);

		// Set sprites
		buttonNormal = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("images/fire_button.png"))));
		buttonPressed = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("images/fire_button_pressed.png"))));

		// Resize
		GameUtils.resize(buttonNormal);
		GameUtils.resize(buttonPressed);

		// Set the bounding box
		boundingBox = new Rectangle();
		boundingBox.y = buttonNormal.getHeight()/2;
		boundingBox.width = buttonNormal.getWidth();
		boundingBox.height = buttonNormal.getHeight();

		if (configuration == ControlsConfiguration.Default)
		{
			boundingBox.x = Gdx.graphics.getWidth() - (buttonNormal.getWidth()*1.5f);
		}
		else
		{
			boundingBox.x = buttonNormal.getHeight()/2;
		}

		positionX = (boundingBox.x + (boundingBox.getWidth()/2)) - (buttonNormal.getWidth()/2);
		positionY = buttonNormal.getHeight()/2;

		buttonNormal.setPosition(positionX, positionY);
		buttonPressed.setPosition(positionX, positionY);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		if (isTouched)
		{
			buttonPressed.draw(batch);
		}
		else
		{
			buttonNormal.draw(batch);
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable)
	{
		if (GameState.currentState == State.Running)
		{
			return (x > boundingBox.x && x < (boundingBox.x+boundingBox.width) && y > boundingBox.y && y < (boundingBox.y+boundingBox.height)) ? this : null;
		}
		else
		{
			return null;
		}
	}

	public void touchDown(float x, float y)
	{
		isTouched = true;
	}

	public void touchUp(float x, float y)
	{
		isTouched = false;
	}
}
