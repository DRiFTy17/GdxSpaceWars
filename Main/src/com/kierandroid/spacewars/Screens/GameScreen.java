package com.kierandroid.spacewars.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kierandroid.spacewars.Controls.CameraController;
import com.kierandroid.spacewars.Controls.Joystick;
import com.kierandroid.spacewars.EntryPoint;
import com.kierandroid.spacewars.Enumerations.JoystickConfiguration;
import com.kierandroid.spacewars.Enumerations.State;
import com.kierandroid.spacewars.GameObjects.Asteroid;
import com.kierandroid.spacewars.GameObjects.Planet;
import com.kierandroid.spacewars.GameObjects.SkySphere;
import com.kierandroid.spacewars.Utilities.GameState;

public class GameScreen extends TransitionScreen
{
	// Our entry context
	private EntryPoint entryPoint;

	// 3D Game Objects
	private Planet _planet;
	private SkySphere _skySphere;
	private Asteroid _asteroid;

	// Actors
	Joystick joystick;

	// Renderers
	private SpriteBatch batch;
	private Stage _stage;

	// Camera
	private PerspectiveCamera camera;
	private CameraController controller;

	// Fonts
	private BitmapFont font;

	/**
	 * Default constructor
	 * @param entryPoint The handle to our entry context
	 */
	public GameScreen(EntryPoint entryPoint)
	{
		super(entryPoint);

		// Our reference to the entry point class
		this.entryPoint = entryPoint;
	}

	/**
	 * Setup the game
	 */
	@Override
	public void create()
	{
		// Create the planet
		_planet = new Planet();

		// Create the skysphere
		_skySphere = new SkySphere();
		_skySphere.mesh.scale(SkySphere.SCALE_FACTOR, SkySphere.SCALE_FACTOR, SkySphere.SCALE_FACTOR);

		// Create the asteroid
		_asteroid = new Asteroid();
		_asteroid.mesh.scale(0.1f, 0.1f, 0.1f);

		// Get the planet dimensions for use in setting up our camera
		float len = _planet.boundingBox.getDimensions().len();

		// Set the camera rotation vector
		camera = new PerspectiveCamera(45, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Vector3.tmp.set(0, 0, -3.5f));
		camera.lookAt(0, 0, 0);
		camera.near = 0.1f;
		camera.far = 100f;
		camera.update();

		// Set up our camera controller
		controller = new CameraController(camera);

		// Renderers
		batch = new SpriteBatch(); // This will draw our BitmapFont
		font = new BitmapFont(); // The font that is used for debugging
		font.setColor(Color.GREEN); // Does anyone know what this does?

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
		joystick = new Joystick(JoystickConfiguration.Left);
		joystick.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				joystick.touchDown(x, y);
				return true;
			}

			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				joystick.touchDragged(x, y);
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				joystick.touchUp(x, y);
			}
		});

		_stage.addActor(joystick);
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

		camera.apply(Gdx.gl10);

		if (_planet != null)
		{
			// Bind our planet texture and render the planet
			gl.glEnable(GL10.GL_TEXTURE_2D);

			_skySphere.render(gl, delta);
			_planet.render(gl, delta);
			_asteroid.render(gl, delta);

			gl.glDisable(GL10.GL_TEXTURE_2D);
		}

		// Swtich to 2D Mode for drawing of the HUD
		gl.glDisable(GL10.GL_DEPTH_TEST);

		Gdx.gl10.glMatrixMode(GL10.GL_PROJECTION);
		Gdx.gl10.glLoadIdentity();
		Gdx.gl10.glOrthof(0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, -1, 1);
		Gdx.gl10.glMatrixMode(GL10.GL_MODELVIEW);
		Gdx.gl10.glLoadIdentity();

		// Act and draw the _stage
		_stage.act(delta);
		_stage.draw();

		// Draw our debugging information
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
		batch.end();

		if (GameState.currentState == State.Running)
		{
			// Move the camera
			if (joystick.isTouched)
			{
				joystick.updateRotationAxis();
				controller.updateRotation(joystick.rotationAxis);
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
		_planet.texture.dispose();
		_skySphere.texture.dispose();
	}
}