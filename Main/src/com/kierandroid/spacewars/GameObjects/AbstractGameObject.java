package com.kierandroid.spacewars.GameObjects;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.kierandroid.spacewars.EntryPoint;
import com.kierandroid.spacewars.Utilities.MeshLoaderResult;

public abstract class AbstractGameObject
{
	protected EntryPoint game;
	protected ImmediateModeRenderer10 renderer;
	protected BoundingBox boundingBox;
	protected Sphere boundingSphere;
	protected Mesh mesh;
	protected Texture texture;
	protected float pitch;
	protected float yaw;
	protected float roll;
	protected Quaternion rotation;
	protected Quaternion newRotation;
	protected float[] rotationMatrix;
	public float localRotation = 0.0f;
	public float orbit = 0.0f;
	public Matrix4 position;
	public Matrix4 newPosition;
	public Vector3 vectorPosition;

	public AbstractGameObject(EntryPoint game, String modelPath, String texturePath, float scaleFactor)
	{
		this.game = game;

		MeshLoaderResult mlr = game.assets.get(modelPath, MeshLoaderResult.class);
		mesh = new Mesh(true, mlr.numFaces * 3, 0, mlr.attributes.toArray(new VertexAttribute[mlr.attributes.size()]));
		mesh.setVertices(mlr.verts);

		if (scaleFactor > 0.0f)
		{
			mesh.scale(scaleFactor, scaleFactor, scaleFactor);
		}

		// Load the texture
		texture = game.assets.get(texturePath);

		// Create our bounding box
		boundingBox = new BoundingBox();
		boundingBox.set(mesh.calculateBoundingBox());

		// Create our bounding sphere
		boundingSphere = new Sphere(boundingBox.getCenter(), boundingBox.getDimensions().len()/2);

		renderer = new ImmediateModeRenderer10();

		rotation = new Quaternion();
		newRotation = new Quaternion();
		rotationMatrix = new float[16];

		position = new Matrix4();
		newPosition = new Matrix4();
		vectorPosition = new Vector3();
	}

	public abstract void render(GL11 gl, float delta);
	public abstract void dispose();

	public void renderBoundingBox(GL10 gl, float delta)
	{
		gl.glColor4f(1, 0, 0, 1);
		renderer.begin(GL10.GL_LINES);
		renderer.vertex(boundingBox.getCorners()[0]);
		renderer.vertex(boundingBox.getCorners()[1]);
		renderer.vertex(boundingBox.getCorners()[2]);
		renderer.vertex(boundingBox.getCorners()[3]);

		renderer.vertex(boundingBox.getCorners()[4]);
		renderer.vertex(boundingBox.getCorners()[5]);
		renderer.vertex(boundingBox.getCorners()[6]);
		renderer.vertex(boundingBox.getCorners()[7]);

		renderer.vertex(boundingBox.getCorners()[0]);
		renderer.vertex(boundingBox.getCorners()[4]);
		renderer.vertex(boundingBox.getCorners()[3]);
		renderer.vertex(boundingBox.getCorners()[7]);

		renderer.vertex(boundingBox.getCorners()[1]);
		renderer.vertex(boundingBox.getCorners()[5]);
		renderer.vertex(boundingBox.getCorners()[2]);
		renderer.vertex(boundingBox.getCorners()[6]);

		renderer.vertex(boundingBox.getCorners()[5]);
		renderer.vertex(boundingBox.getCorners()[6]);
		renderer.vertex(boundingBox.getCorners()[4]);
		renderer.vertex(boundingBox.getCorners()[7]);

		renderer.vertex(boundingBox.getCorners()[0]);
		renderer.vertex(boundingBox.getCorners()[3]);
		renderer.vertex(boundingBox.getCorners()[1]);
		renderer.vertex(boundingBox.getCorners()[2]);
		renderer.end();
		gl.glColor4f(1, 1, 1, 1);
	}
}
