package com.kierandroid.spacewars.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kierandroid.spacewars.EntryPoint;

public abstract class TransitionScreen implements Screen {
   static final float FADE_DURATION = 0.5f;

   public EntryPoint game;
   SpriteBatch batch;
   OrthographicCamera guiCam;
   
   float fadeTime = 0;
   float fadeAlpha = 0;
   
   Screen nextScreen;
   boolean goingToNextScreen = false;
   boolean enteringScreen = false;
   Texture texture;
   Sprite sprite;

   public TransitionScreen(EntryPoint game)
   {
      this.game = game;

      texture = new Texture(Gdx.files.internal("images/blanket.png"));
      sprite = new Sprite(texture);
      sprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      
      guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      guiCam.position.set((Gdx.graphics.getWidth()/2), (Gdx.graphics.getHeight()/2), 0);
      
      batch = new SpriteBatch();
      
      fadeTime = 0;
      enteringScreen = true;
      create();
   }

   /**
    * Call this method to change screen using a transition.
    */
   public void nextScreen(Screen screen)
   {
      fadeTime = 0;
      goingToNextScreen = true;
      nextScreen = screen;
   }

   /**
    * Screen initialization goes here.
    */
   public abstract void create();
   
   /**
    * Screen rendering goes here.
    */
   public abstract void renderScreen(float delta);

   @Override
   public void render(float delta)
   {
      if (delta > 0.1f)
         delta = 0.1f;
      
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
      
      renderScreen(delta);
      
      // fadein when entering the screen
      if (enteringScreen) {
         
         fadeTime += delta;
         
         if (fadeTime == 0) {
            fadeAlpha = 1;
         }
         else {
            fadeAlpha = 1-(1/FADE_DURATION)*fadeTime;
            
            if (fadeAlpha < 0) fadeAlpha = 0;
            if (fadeAlpha > 1) fadeAlpha = 1;
         }
         
         if (fadeTime >= FADE_DURATION) {
            
            fadeTime = FADE_DURATION;
            enteringScreen = false;
         }
         
         batch.begin();
         batch.enableBlending();
         sprite.draw(batch, fadeAlpha);
         batch.end();
      }
      
      // fadeout when leaving
      if (goingToNextScreen) {
         
         fadeTime += delta;
         
         if (fadeTime == 0) {
            fadeAlpha = 0;
         }
         else {
            fadeAlpha = (1/FADE_DURATION)*fadeTime;
            
            if (fadeAlpha < 0) fadeAlpha = 0;
            if (fadeAlpha > 1) fadeAlpha = 1;
         }
         
         if (fadeTime >= FADE_DURATION) {
            
            fadeTime = FADE_DURATION;
            game.setScreen(nextScreen);
            goingToNextScreen = false;
         }
         
         batch.begin();
         batch.enableBlending();
         sprite.draw(batch, fadeAlpha);
         batch.end();
      }
   }

   @Override
   public void resize(int width, int height)
   {
   }

   @Override
   public void show()
   {
   }

   @Override
   public void hide()
   {
   }

   @Override
   public void pause()
   {
   }

   @Override
   public void resume()
   {
   }

   @Override
   public void dispose()
   {
   }
}