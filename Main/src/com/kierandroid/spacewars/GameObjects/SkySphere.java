package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;

public class SkySphere
{
	public static final float ROTATION_SPEED = 2.0f;
	public static final float SCALE_FACTOR = 4.0f;

	public Mesh mesh;
	public Texture texture;
	public float rotation = 0.0f;

	public SkySphere()
	{
		// Load the sphere model
		mesh = ObjLoader.loadObj(Gdx.files.internal("models/sphere.obj").read(), true);

		// Load the texture
		texture = new Texture(Gdx.files.internal("textures/space.png"));
	}

	public void render(GL10 gl, float delta)
	{
		gl.glCullFace(GL10.GL_FRONT);

		gl.glPushMatrix();
		rotation = (rotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(rotation, 1, 1, 1);
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
	}
}
