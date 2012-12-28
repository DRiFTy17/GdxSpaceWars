package com.kierandroid.spacewars.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kierandroid.spacewars.EntryPoint;
import com.kierandroid.spacewars.Utilities.MeshLoaderResult;
import com.kierandroid.spacewars.loaders.MeshLoader;

public class LoadingScreen extends TransitionScreen
{

	private Stage _stage;

	private Image _background;
	private Image _logo;
	private Image _loadingFrame;
	private Image _loadingFrameBackground;
	private Image _loadingBar;
	private Image _loadingHidden;

	private float startX, endX;
	private float percent;
	private BitmapFont _font;
	private SpriteBatch _batch;

	public LoadingScreen(EntryPoint _context)
	{
		super(_context);
	}

	@Override
	public void create()
	{
		game.assets.load("textures/skysphere.png", Texture.class);
		game.assets.load("images/libgdx_logo.png", Texture.class);
		game.assets.load("images/loading_bar.png", Texture.class);
		game.assets.load("images/loading_frame.png", Texture.class);
		game.assets.load("images/loading_frame_bg.png", Texture.class);
		game.assets.load("images/loading_bar_hidden.png", Texture.class);

		game.assets.finishLoading();

		_stage = new Stage();

		_background = new Image(game.assets.get("textures/skysphere.png", Texture.class));
		_background.setWidth(Gdx.graphics.getWidth());
		_background.setHeight(Gdx.graphics.getHeight());

		_logo = new Image(game.assets.get("images/libgdx_logo.png", Texture.class));
		_logo.setPosition((Gdx.graphics.getWidth()-_logo.getWidth())/2, Gdx.graphics.getHeight()-_logo.getHeight());

		_loadingFrame = new Image(game.assets.get("images/loading_frame.png", Texture.class));
		_loadingFrame.setX((Gdx.graphics.getWidth()-_loadingFrame.getWidth())/2);
		_loadingFrame.setY((Gdx.graphics.getHeight()-_loadingFrame.getHeight())/2);

		_loadingBar = new Image(game.assets.get("images/loading_bar.png", Texture.class));
		_loadingBar.setPosition(_loadingFrame.getX(), _loadingFrame.getY());

		_loadingHidden = new Image(game.assets.get("images/loading_bar_hidden.png", Texture.class));
		_loadingHidden.setPosition(_loadingBar.getX(), _loadingBar.getY());

		_loadingFrameBackground = new Image(game.assets.get("images/loading_frame_bg.png", Texture.class));
		_loadingFrameBackground.setSize(_loadingFrame.getWidth(), _loadingFrame.getHeight());
		_loadingFrameBackground.setX(_loadingHidden.getX());
		_loadingFrameBackground.setY(_loadingFrame.getY());

		startX = _loadingHidden.getX();
		endX = _loadingFrame.getWidth();

		_loadingFrameBackground.setSize(_loadingFrame.getWidth(), _loadingFrame.getHeight());
		_loadingFrameBackground.setX(_loadingHidden.getX());
		_loadingFrameBackground.setY(_loadingHidden.getY());

		_stage.addActor(_background);
		_stage.addActor(_loadingBar);
		_stage.addActor(_loadingFrameBackground);
		_stage.addActor(_loadingHidden);
		_stage.addActor(_loadingFrame);
		_stage.addActor(_logo);

		game.assets.load("textures/atmosphere.png", Texture.class);
		game.assets.load("textures/atmosphere_blue.png", Texture.class);
		game.assets.load("textures/moon.png", Texture.class);
		game.assets.load("textures/moon_orange.png", Texture.class);
		game.assets.load("images/fire_button.png", Texture.class);
		game.assets.load("images/fire_button_pressed.png", Texture.class);
		game.assets.load("images/joystick.png", Texture.class);
		game.assets.load("images/joystick_background.png", Texture.class);

		game.assets.setLoader(MeshLoaderResult.class, new MeshLoader(new InternalFileHandleResolver()));
		game.assets.load("models/planet.obj", MeshLoaderResult.class);

		_font = new BitmapFont();
		_font.setColor(Color.WHITE);
		_font.setScale(1.5f);

		_batch = new SpriteBatch();
	}

	@Override
	public void renderScreen(float delta)
	{
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

		if (game.assets.update()) {
			if (Gdx.input.isTouched()) {
				game.setScreen(new GameScreen(game));
			}
		}

		percent = Interpolation.linear.apply(percent, game.assets.getProgress(), 0.1f);

		_loadingHidden.setX(startX + endX * percent);
		_loadingFrameBackground.setX(_loadingHidden.getX());
		_loadingFrameBackground.setWidth(_loadingFrame.getWidth() - _loadingFrame.getWidth() * percent);
		_loadingFrameBackground.invalidate();

		_stage.act();
		_stage.draw();

		_batch.begin();
		_font.draw(_batch, (int)Math.ceil(game.assets.getProgress()*100) + "%", Gdx.graphics.getWidth()/2-30, _loadingFrame.getY()+_loadingFrame.getHeight()+20);

		if (game.assets.update())
		{
			_font.draw(_batch, "Loading Complete.", Gdx.graphics.getWidth()/2-90, _loadingFrame.getY()-50);
		}

		_batch.end();
	}

	@Override
	public void hide()
	{
	}

	@Override
	public void dispose()
	{
		_stage.dispose();
		game.assets.unload("images/libgdx_logo.png");
	}
}
