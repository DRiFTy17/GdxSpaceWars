package com.kierandroid.spacewars.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kierandroid.spacewars.EntryPoint;
import com.kierandroid.spacewars.Enumerations.JoystickConfiguration;
import com.kierandroid.spacewars.Enumerations.State;
import com.kierandroid.spacewars.Math.MathHelper;
import com.kierandroid.spacewars.Math.Vector2d;
import com.kierandroid.spacewars.Utilities.GameState;
import com.kierandroid.spacewars.Utilities.GameUtils;

public class Joystick extends Actor
{
	private EntryPoint game;

	public Rectangle boundingBox;
	public Circle boundingCircle;
	public Sprite backgroundSprite;
	public Sprite joystickSprite;
	public float joystickStartX;
	public float joystickStartY;
	public boolean isTouched = false;
	public Vector2d center;
	public Vector2d position;
	public Vector2d rotationAxis;
	
	public Joystick(EntryPoint game, JoystickConfiguration configuration, String backgroundTexture, String foregroundTexture)
	{
		this.game = game;

		this.setTouchable(Touchable.enabled);

		position = new Vector2d();

		// Set sprites
		//backgroundSprite = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("images/joystick_background.png"))));
		backgroundSprite = new Sprite(game.assets.get(backgroundTexture, Texture.class));
		//joystickSprite = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("images/joystick.png"))));
		joystickSprite = new Sprite(game.assets.get(foregroundTexture, Texture.class));
		
		// Resize
		GameUtils.resize(backgroundSprite);
		GameUtils.resize(joystickSprite);

		// Create a default rotation axis
		rotationAxis = new Vector2d();

		// Set the bounding box
		boundingBox = new Rectangle();
		boundingBox.y = 0;
		boundingBox.width = backgroundSprite.getWidth();
		boundingBox.height = backgroundSprite.getHeight();

		if (configuration == JoystickConfiguration.Left)
		{
			boundingBox.x = 0;
		}
		else
		{
			boundingBox.x = Gdx.graphics.getWidth()-backgroundSprite.getWidth();
		}

		// Set the joystick x and y
		joystickStartX = (boundingBox.x + (boundingBox.getWidth()/2)) - (joystickSprite.getWidth()/2);
		joystickStartY = (backgroundSprite.getHeight()/2)-(joystickSprite.getWidth()/2);

		boundingCircle = new Circle();
		boundingCircle.set(joystickStartX, joystickStartY, (boundingBox.getWidth()/2));

		// Set sprite positions
		backgroundSprite.setPosition(boundingBox.x, boundingBox.y);
		joystickSprite.setPosition(joystickStartX, joystickStartY);
		
		// Set center x and y
		center = new Vector2d();
		center.X = boundingBox.x + (backgroundSprite.getWidth()/2);
		center.Y = boundingBox.y + (backgroundSprite.getHeight()/2);
	}
	
	private void checkRectangleCollision()
	{
		float x = joystickSprite.getX()+(joystickSprite.getWidth()/2);
		float y = joystickSprite.getY()+(joystickSprite.getHeight()/2);
		float left = boundingBox.x;
		float top = boundingBox.y+boundingBox.height;
		float right = boundingBox.x+boundingBox.width;
		float bottom = boundingBox.y;
		
		// Check left and right
		if (x < left)
		{
			joystickSprite.setX(left-(joystickSprite.getWidth()/2));
		}
		else if (x > right)
		{
			joystickSprite.setX(right-(joystickSprite.getWidth()/2));
		}
		
		// Check top and bottom
		if (y > top)
		{
			joystickSprite.setY(top-(joystickSprite.getHeight()/2));
		}
		else if (y < bottom)
		{
			joystickSprite.setY(bottom-(joystickSprite.getHeight()/2));
		}
	}

	private void checkCircleCollision()
	{
		// Get current x and y of center of joystick
		float x = joystickSprite.getX();
		float y = joystickSprite.getY();

		// If the current x and y is not within the circle then put it in the circle
		if (!boundingCircle.contains(x, y))
		{
			// Get the current joystick angle in radians
			double angle = (getAngle()+90) * (Math.PI/180);

			// Set the x and y coordinate of the joystick to the point on the circle
			joystickSprite.setX((float)(boundingCircle.x + Math.cos(angle) * boundingCircle.radius));
			joystickSprite.setY((float)(boundingCircle.y + Math.sin(angle) * boundingCircle.radius));
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		checkCircleCollision();

		backgroundSprite.draw(batch);
		joystickSprite.draw(batch);
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
		position.X = x;
		position.Y = y;

		isTouched = true;
		joystickSprite.setPosition(x-(joystickSprite.getWidth()/2), y-(joystickSprite.getHeight()/2));
	}

	public void touchDragged(float x, float y) {
		position.X = x;
		position.Y = y;
		isTouched = true;
		joystickSprite.setPosition(x-(joystickSprite.getWidth()/2), y-(joystickSprite.getHeight()/2));
	}

	public void touchUp(float x, float y)
	{
		position.X = x;
		position.Y = y;
		isTouched = false;
		joystickSprite.setPosition(joystickStartX, joystickStartY);
	}

	public float getAngle()
	{
		return Vector2d.getAngleBetweenTwoPoints(position.X, center.X, position.Y, center.Y);
	}

	public float getPitch()
	{
		return MathHelper.clamp((position.X - center.X), -CameraController.CAMERA_SPEED_MAX, CameraController.CAMERA_SPEED_MAX) * MathHelper.PIOVER180;
	}

	public float getYaw()
	{
		return MathHelper.clamp((position.Y - center.Y), -CameraController.CAMERA_SPEED_MAX, CameraController.CAMERA_SPEED_MAX) * MathHelper.PIOVER180;
	}
}
