package com.kierandroid.spacewars.Controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class CameraController
{
	// How fast we CAN rotate and move
	private final float ROTATION_SPEED = 25.23f;

	// Camera constants
	public static final float CAMERA_DISTANCE = 3.5f;
	public static final float CAMERA_SPEED_MAX = 75.0f;

	// Camera settings
	private final float FOV = 45.0f;
	private final Vector3 LOOK_AT_POINT = new Vector3(0, 0, 0);
	private final float NEAR = 0.1f;
	private final float FAR = 100.0f;

	// The camera we are controlling
	public PerspectiveCamera camera;

	// The orientation of the camera represented by a rotation matrix
	public Quaternion rotation;
	private Quaternion newRotation;

	Vector3 right = new Vector3();

	/**
	 * The Constructor
	 */
	public CameraController()
	{
		// Set the camera we are controlling
		camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Vector3.tmp.set(0, 0, -CAMERA_DISTANCE));
		camera.lookAt(LOOK_AT_POINT.x, LOOK_AT_POINT.y, LOOK_AT_POINT.z);
		camera.near = NEAR;
		camera.far = FAR;
		camera.update();

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
		camera.view.setToLookAt(camera.position, Vector3.tmp.set(camera.position).add(camera.direction), camera.up);

		camera.view.rotate(rotation);
		camera.lookAt(0, 0, 0);

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

	public void rotate(float pitch, float yaw, float roll)
	{
		newRotation.setEulerAngles(-pitch, -yaw, roll);
		rotation.mulLeft(newRotation);
	}
}
