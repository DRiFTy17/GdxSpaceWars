package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.kierandroid.spacewars.EntryPoint;

public class SkySphere extends AbstractGameObject
{
	public static final float ROTATION_SPEED = 2.0f;
	public static final float SCALE_FACTOR = 4.0f;

	public SkySphere(EntryPoint game, String modelPath, String texturePath)
	{
		super(game, modelPath, texturePath, SCALE_FACTOR);
	}

	@Override
	public void render(GL11 gl, float delta)
	{
		gl.glCullFace(GL10.GL_FRONT);

		gl.glPushMatrix();
		localRotation = (localRotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(localRotation, 1, 1, 1);
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
	}

	@Override
	public void dispose()
	{
		texture.dispose();
		mesh.dispose();
	}
}
