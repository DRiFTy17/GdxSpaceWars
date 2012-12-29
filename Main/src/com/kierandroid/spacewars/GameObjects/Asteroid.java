package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.kierandroid.spacewars.EntryPoint;
import com.kierandroid.spacewars.Math.MathHelper;
import com.kierandroid.spacewars.Utilities.GameState;

public class Asteroid extends AbstractGameObject
{
	public static final float ROTATION_SPEED = 100.0f;
	public static final float ORBIT_SPEED = 25.0f;
	public static final float ORBIT_DISTANCE = 1.20f;
	public static final float SCALE_FACTOR = 0.1f;

	public boolean which;

	public Asteroid(EntryPoint game, String modelPath, String texturePath, boolean which)
	{
		super(game, modelPath, texturePath, SCALE_FACTOR);

		this.pitch = 45* MathHelper.PIOVER180;
		this.yaw = 45* MathHelper.PIOVER180;

		this.which = which;
	}

	@Override
	public void render(GL11 gl, float delta)
	{
		newRotation.setEulerAngles(pitch, yaw, roll);
		rotation.mulLeft(newRotation);
		newRotation.toMatrix(rotationMatrix);

		//gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// Don't draw the back faces of this model
		gl.glCullFace(GL10.GL_BACK);

		// Save the current matrix
		gl.glPushMatrix();

		position.idt();

		// Update the orbit value of this model
		orbit = (orbit + ORBIT_SPEED * delta) % 360;

		if (which)
		{
			gl.glRotatef(orbit, 1.0f, 1.0f, 0);
			position.rotate(1.0f, 1.0f, 0, orbit);
		}
		else
		{
			gl.glRotatef(orbit, -1.0f, 1.0f, 0);
			position.rotate(-1.0f, 1.0f, 0, orbit);
		}

		//gl.glMatrixMode(GL10.GL_MODELVIEW);
		//gl.glMultMatrixf(rotationMatrix, 0);

		// Move the model to it's specified radius
		gl.glTranslatef(0, 0, -ORBIT_DISTANCE);

		position.translate(0, 0, -ORBIT_DISTANCE);
//		boundingBox.mul(position);

		// Spin the model
		localRotation = (localRotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(localRotation, 1, 1, 1);

		position.rotate(1.0f, 1.0f, 1.0f, localRotation);

		boundingSphere.center.x = getX();
		boundingSphere.center.y = getY();
		boundingSphere.center.z = getZ();

		if (GameState.DEBUG)
		{
			renderBoundingBox(gl, delta);
		}

		// Bind the texture and draw
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);

		// Restore the matrix
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
