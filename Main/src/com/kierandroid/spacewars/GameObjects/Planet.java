package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;

public class Planet
{
	public static final float ROTATION_SPEED = 10.0f;

	public float rotation = 0.0f;
	public BoundingBox boundingBox;
	public Sphere boundingSphere;
	public Mesh mesh;
	public Texture texture;

	public Planet()
	{
		// Load the sphere model
		mesh = ObjLoader.loadObj(Gdx.files.internal("models/planet.obj").read(), true);

		// Load the texture
		texture = new Texture(Gdx.files.internal("textures/moon_orange.png"));

		// Create our bounding box
		boundingBox = new BoundingBox();
		boundingBox.set(mesh.calculateBoundingBox());

		// Create our bounding sphere
		boundingSphere = new Sphere(boundingBox.getCenter(), boundingBox.getDimensions().len()/2);
	}

	public void render(GL10 gl, float delta)
	{
		//gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glCullFace(GL10.GL_BACK);

		gl.glPushMatrix();

		rotation = (rotation + ROTATION_SPEED * delta) % 360;
		gl.glRotatef(rotation, 1, 1, 1);
		texture.bind();
		mesh.render(GL10.GL_TRIANGLES);

		gl.glPopMatrix();

		//gl.glDisable(GL10.GL_BLEND);
	}

	public void dispose()
	{
		texture.dispose();
		mesh.dispose();
	}
}
