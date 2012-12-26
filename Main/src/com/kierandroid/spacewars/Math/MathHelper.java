package com.kierandroid.spacewars.Math;

import com.badlogic.gdx.math.Vector3;

public class MathHelper
{
	public static final float PIOVER180 = (float)(Math.PI/180);
	public static final float PIOVER2 = (float)(Math.PI/2);

	/**
	 * Makes sure a values doesn't exceed it's minumum or maxiumum values
	 * @param value The values to check
	 * @param min The minimum value
	 * @param max The maxiumum value
	 * @return
	 */
	public static float clamp(float value, float min, float max)
	{
		if (value < min)
		{
			return min;
		}
		else if(value > max)
		{
			return max;
		}
		else
		{
			return value;
		}
	}

	public static Vector3 sphericalToCartesian(float radius, float theta, float phi)
	{
		Vector3 outCart = new Vector3();
		theta *= Math.PI/180;
		phi *= Math.PI/180;
		float a = radius * (float) Math.cos(theta);

		outCart.x = a * (float) Math.cos(phi);
		outCart.y = radius * (float) Math.sin(theta);
		outCart.z = a * (float) Math.sin(phi);

		return outCart;
	}

	public static float[] cartesianToSpherical(Vector3 cartCoords)
	{
		float outRho = (float) Math.sqrt((cartCoords.x * cartCoords.x) + (cartCoords.y * cartCoords.y) + (cartCoords.z * cartCoords.z));
		float outPhi = (float) Math.atan(cartCoords.z / cartCoords.x);

		if (cartCoords.x < 0)
		{
			outPhi += Math.PI;
		}

		float outTheta = (float) Math.asin(cartCoords.y / outRho);

		return new float[] { outRho, outPhi, outTheta };
	}

	/**
	 * Rotates a Quaternion
	 * @param heading
	 * @param attitude
	 * @param bank
	 */
	/*
	public final void rotate(double heading, double attitude, double bank) {
		// Assuming the angles are in radians.
		double c1 = Math.cos(heading/2);
		double s1 = Math.sin(heading/2);
		double c2 = Math.cos(attitude/2);
		double s2 = Math.sin(attitude/2);
		double c3 = Math.cos(bank/2);
		double s3 = Math.sin(bank/2);
		double c1c2 = c1*c2;
		double s1s2 = s1*s2;

		w =c1c2*c3 - s1s2*s3;
		x =c1c2*s3 + s1s2*c3;
		y =s1*c2*c3 + c1*s2*s3;
		z =c1*s2*c3 - s1*c2*s3;
	}
	*/
}
