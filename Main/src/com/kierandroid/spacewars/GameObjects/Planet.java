package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.kierandroid.spacewars.EntryPoint;

public class Planet extends AbstractGameObject
{
	public static final float ROTATION_SPEED = 10.0f;

	public Planet(EntryPoint game, String model, String texture)
	{
		super(game, model, texture, 0.0f);
	}

	@Override
	public void render(GL11 gl, float delta)
	{
		//gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glCullFace(GL10.GL_BACK);

		gl.glPushMatrix();

		localRotation = (localRotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(localRotation, 1, 1, 1);

		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);

		gl.glPopMatrix();

		//gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void dispose()
	{
		texture.dispose();
		mesh.dispose();
	}
}
