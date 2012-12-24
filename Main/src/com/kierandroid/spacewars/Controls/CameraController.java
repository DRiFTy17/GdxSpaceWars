package com.kierandroid.spacewars.Controls;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.kierandroid.spacewars.Math.Vector2d;

public class CameraController
{
	// How fast we CAN rotate and move
	private final float ROTATION_SPEED = 25.23f;

	// The camera we are controlling
	private PerspectiveCamera camera;

	// The orientation of the camera represented by a rotation matrix
	public Quaternion rotation;
	private Quaternion newRotation;

	final Vector3 tmp = new Vector3();
	Vector3 right = new Vector3();

	/**
	 * The Constructor
	 * @param camera
	 */
	public CameraController(PerspectiveCamera camera)
	{
		// Set the camera we are controlling
		this.camera = camera;

		// Set up our rotation quaternion
		rotation = new Quaternion();
		newRotation = new Quaternion();
	}

	/**
	 * Update the camera using the new rotation
	 * @param updateFrustum
	 */
	public void update(boolean updateFrustum)
	{
		float aspect = camera.viewportWidth / camera.viewportHeight;
		camera.projection.setToProjection(Math.abs(camera.near), Math.abs(camera.far), camera.fieldOfView, aspect);
		camera.view.setToLookAt(camera.position, tmp.set(camera.position).add(camera.direction), camera.up);

		camera.view.rotate(rotation);

		// This moves the camera eye/look at point
		//rotation.transform(camera.direction);
		//rotation.transform(camera.up);

		camera.combined.set(camera.projection);
		Matrix4.mul(camera.combined.val, camera.view.val);

		if (updateFrustum)
		{
			camera.invProjectionView.set(camera.combined);
			Matrix4.inv(camera.invProjectionView.val);
			camera.frustum.update(camera.invProjectionView);
		}
	}

	/**
	 * Updates the rotation quaternion using euler angles
	 * @param axis
	 */
	public void updateRotation(Vector2d axis)
	{
		// Update quaternion
		//newRotation.setFromAxis(Vector3.tmp.set(axisX, axisY, axisZ), ROTATION_SPEED * speed * MathHelper.PIOVER180);
		newRotation.setEulerAngles(axis.Y, axis.X, 0);
		//rotation.mul(newRotation);
		rotation.mulLeft(newRotation);

		update(true);
	}

	/**
	 * Move the camera to the left
	 * @param amount
	 */
	public void moveLeft(float amount)
	{
		right = camera.direction.cpy().crs(camera.up);
		camera.position.set(camera.position.add(right.mul(-amount).mul(camera.position.len())).nor());
		camera.direction.set(Vector3.tmp.set(0, 0, 0).sub(camera.position).nor());
		right.set(camera.direction.cpy().crs(camera.up));
		camera.up.set(right.crs(camera.direction));
		camera.update();
	}

	/**
	 * Move the camera to the right
	 * @param amount
	 */
	public void moveRight(float amount)
	{
		right = camera.direction.cpy().crs(camera.up);
		camera.position.set(camera.position.add(right.mul(amount).mul(camera.position.len())).nor());
		camera.direction.set(Vector3.tmp.set(0, 0, 0).sub(camera.position).nor());
		right.set(camera.direction.cpy().crs(camera.up));
		camera.up.set(right.crs(camera.direction));
		camera.update();
	}

	/**
	 * Move the camera up
	 * @param amount
	 */
	public void moveUp(float amount)
	{
		right = camera.direction.cpy().crs(camera.up).nor();
		camera.position.set(camera.position.add(camera.up.mul(amount)).mul(camera.position.len()).nor());
		camera.direction.set(Vector3.tmp.set(0, 0, 0).sub(camera.position).nor());
		camera.up.set(right.crs(camera.direction));
		camera.translate(0, 0, -3);
		camera.update();
	}

	/**
	 * Move the camera down
	 * @param amount
	 */
	public void moveDown(float amount)
	{
		right = camera.direction.cpy().crs(camera.up).nor();
		camera.position.set(camera.position.add(camera.up.mul(-amount)).mul(camera.position.len()).nor());
		camera.direction.set(Vector3.tmp.set(0, 0, 0).sub(camera.position).nor());
		camera.up.set(right.crs(camera.direction));
		camera.translate(0, 0, -3);
		camera.update();
	}
}
