package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;

public class Asteroid
{
	public static final float ROTATION_SPEED = 100.0f;
	public static final float ORBIT_SPEED = 50.0f;

	public float rotation = 0.0f;
	public float orbit = 0.0f;
	public BoundingBox boundingBox;
	public Sphere boundingSphere;
	public Mesh mesh;
	public Texture texture;

	public Asteroid()
	{
		// Load the sphere model
		mesh = ObjLoader.loadObj(Gdx.files.internal("models/sphere.obj").read(), true);

		// Load the texture
		texture = new Texture(Gdx.files.internal("textures/moon.png"));

		// Create our bounding box
		boundingBox = new BoundingBox();
		boundingBox.set(mesh.calculateBoundingBox());

		// Create our bounding sphere
		boundingSphere = new Sphere(boundingBox.getCenter(), boundingBox.getDimensions().len()/2);
	}

	public void render(GL10 gl, float delta)
	{
		gl.glCullFace(GL10.GL_BACK);

		gl.glPushMatrix();
		orbit = (orbit + ORBIT_SPEED * delta) % 360;
		gl.glRotatef(orbit, 1, 1, 0);
		gl.glTranslatef(0, 0, -1.15f);
		rotation = (rotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(rotation, 1, 1, 1);
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);
		gl.glPopMatrix();
	}
}
