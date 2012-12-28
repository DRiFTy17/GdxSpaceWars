package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.kierandroid.spacewars.EntryPoint;

public class Atmosphere extends AbstractGameObject
{
	public static final float ROTATION_SPEED = 5.0f;
	public static final float SCALE_FACTOR = 1.1f;

	public Atmosphere(EntryPoint game, String modelPath, String texturePath)
	{
		super(game, modelPath, texturePath, SCALE_FACTOR);
	}

	@Override
	public void render(GL11 gl, float delta)
	{
		// Turn on blending for our texture
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE_MINUS_SRC_ALPHA, GL10.GL_SRC_ALPHA);

		gl.glCullFace(GL10.GL_BACK);

		gl.glPushMatrix();

		localRotation = (localRotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(localRotation, -1, -1, -1);

		//gl.glColor4f(0.52f, 0.84f, 0.98f, 1);
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);
		//gl.glColor4f(1, 1, 1, 1);

		gl.glPopMatrix();

		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void dispose()
	{
		texture.dispose();
		mesh.dispose();
	}
}
