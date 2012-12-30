package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.math.Quaternion;
import com.kierandroid.spacewars.EntryPoint;

public class Ship extends AbstractGameObject
{
	public static final float ROTATION_SPEED = 10.0f;
	private float posX, posY;

	public Ship(EntryPoint game, String model, String texture)
	{
		super(game, model, texture, 0.1f);
	}

	@Override
	public void render(GL11 gl, float delta)
	{
		gl.glCullFace(GL10.GL_BACK);

		gl.glPushMatrix();

//		gl.glLoadIdentity();

		gl.glLoadMatrixf(rotationMatrix, 0);

		gl.glTranslatef(0, 0, -1.5f);

//		localRotation = (localRotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(localRotation, 0, 0, 1);

		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);

		gl.glPopMatrix();
	}

	public void update(Quaternion newRotation, float localRotation)
	{
		position.rotate(newRotation);
		rotation.toMatrix(rotationMatrix);

		this.localRotation = localRotation;
	}

	@Override
	public void dispose()
	{
		texture.dispose();
		mesh.dispose();
	}
}
