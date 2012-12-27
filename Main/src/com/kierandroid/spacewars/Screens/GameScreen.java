package com.kierandroid.spacewars.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kierandroid.spacewars.Controls.CameraController;
import com.kierandroid.spacewars.Controls.FireButton;
import com.kierandroid.spacewars.Controls.Joystick;
import com.kierandroid.spacewars.EntryPoint;
import com.kierandroid.spacewars.Enumerations.ControlsConfiguration;
import com.kierandroid.spacewars.Enumerations.State;
import com.kierandroid.spacewars.GameObjects.Asteroid;
import com.kierandroid.spacewars.GameObjects.Atmosphere;
import com.kierandroid.spacewars.GameObjects.Planet;
import com.kierandroid.spacewars.GameObjects.SkySphere;
import com.kierandroid.spacewars.Utilities.GameState;

public class GameScreen extends TransitionScreen
{
	// Our entry context
	private EntryPoint _entryPoint;

	// 3D Game Objects
	private Planet _planet;
	private Atmosphere _atmosphere;
	private SkySphere _skySphere;
	private Asteroid _asteroid;

	// Actors
	Joystick _joystick;
	FireButton _fireButton;

	// Renderers
	private SpriteBatch _batch;
	private Stage _stage;

	// Camera
	private CameraController _controller;

	// Fonts
	private BitmapFont _font;

	private ControlsConfiguration _controlsConfiguration;

	/**
	 * Default constructor
	 * @param entryPoint The handle to our entry context
	 */
	public GameScreen(EntryPoint entryPoint)
	{
		super(entryPoint);

		// Our reference to the entry point class
		this._entryPoint = entryPoint;
	}

	/**
	 * Setup the game
	 */
	@Override
	public void create()
	{
		// Turn debugging on or off
		GameState.DEBUG = true;

		// Set the default controls configuration
		_controlsConfiguration = ControlsConfiguration.Default;

		// Create the planet
		_planet = new Planet();

		_atmosphere = new Atmosphere();
		_atmosphere.mesh.scale(1.1f, 1.1f, 1.1f);

		// Create the skysphere
		_skySphere = new SkySphere();
		_skySphere.mesh.scale(SkySphere.SCALE_FACTOR, SkySphere.SCALE_FACTOR, SkySphere.SCALE_FACTOR);

		// Create the asteroid
		_asteroid = new Asteroid();
		_asteroid.mesh.scale(0.1f, 0.1f, 0.1f);

		// Set up our camera _controller
		_controller = new CameraController();

		// Renderers
		_batch = new SpriteBatch();
		_font = new BitmapFont();
		_font.setColor(Color.GREEN);

		// Stage
		_stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); // Draws our HUD

		// Set our 2D actors
		setActors();

		// Catch the input on the _stage and handle the back key presses
		Gdx.input.setInputProcessor(_stage);
		Gdx.input.setCatchBackKey(true);

		// State variables
		GameState.currentState = State.Running;
		GameState.SCORE = 0;
	}

	/**
	 * Creates and adds our 2D actors to the _stage
	 */
	private void setActors()
	{
		_joystick = new Joystick(_controlsConfiguration);
		_joystick.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				_joystick.touchDown(x, y);
				return true;
			}

			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				_joystick.touchDragged(x, y);
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				_joystick.touchUp(x, y);
			}
		});

		_fireButton = new FireButton(_controlsConfiguration);
		//_fireButton.setPositionY(_joystick.center.Y);
		_fireButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				_fireButton.touchDown(x, y);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				_fireButton.touchUp(x, y);
			}
		});

		_stage.addActor(_joystick);
		_stage.addActor(_fireButton);
	}

	/**
	 * Draw the scene
	 * @param delta The time between the last frame and the current frame
	 */
	@Override
	public void renderScreen(float delta)
	{
		GL10 gl = Gdx.app.getGraphics().getGL10();

		// Draw a black background and clear the screen
		gl.glClearColor(0, 0, 0, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_FLAT);

		_controller.camera.apply(Gdx.gl10);

		_skySphere.render(gl, delta);
		_planet.render(gl, delta);
		_atmosphere.render(gl, delta);
		_asteroid.render(gl, delta);

		// Swtich to 2D Mode for drawing of the HUD
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glDisable(GL10.GL_TEXTURE_2D);

		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
		Gdx.gl10.glLoadIdentity();
		Gdx.gl10.glOrthof(0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, -1, 1);
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadIdentity();

		// Act and draw the _stage
		_stage.act(delta);
		_stage.draw();

		// Draw our debugging information
		if (GameState.DEBUG)
		{
			_batch.begin();
			_font.draw(_batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
			_batch.end();
		}

		if (GameState.currentState == State.Running)
		{
			if (_joystick.isTouched)
			{
				_controller.rotate(_joystick.getPitch(), _joystick.getYaw(), 0.0f);
				_controller.update(true);
			}
		}
	}

	/**
	 * What happens when the screen comes into view
	 */
	@Override
	public void show()
	{
	}

	/**
	 * What happens when the screen is hidden
	 */
	@Override
	public void hide()
	{
		Gdx.input.setCatchBackKey(false);
	}

	/**
	 * What happens when the screen is paused
	 */
	@Override
	public void pause()
	{
	}

	/**
	 * What happens when the screen is resumed
	 */
	@Override
	public void resume()
	{
	}

	/**
	 * Release resources
	 */
	@Override
	public void dispose()
	{
		_stage.dispose();

		_planet.dispose();
		_atmosphere.dispose();
		_asteroid.dispose();
		_skySphere.dispose();
	}
}