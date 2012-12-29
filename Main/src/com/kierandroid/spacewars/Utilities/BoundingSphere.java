package com.kierandroid.spacewars.Utilities;

import com.badlogic.gdx.math.Vector3;

public class BoundingSphere
{
	// The radius of the sphere
	public float radius;

	// The center of the sphere
	public Vector3 center;

	/** Constructs a sphere with the given center and radius
	 * @param center The center
	 * @param radius The radius
	 */
	public BoundingSphere (Vector3 center, float radius)
	{
		this.center = new Vector3(center);
		this.radius = radius;
	}

	/** @param sphere the other sphere
	 * @return whether this and the other sphere overlap
	 */
	public boolean overlaps (BoundingSphere sphere)
	{
		return center.dst2(sphere.center) < (radius+sphere.radius)*(radius+sphere.radius);
	}
}
