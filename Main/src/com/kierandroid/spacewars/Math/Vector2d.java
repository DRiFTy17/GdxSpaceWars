package com.kierandroid.spacewars.Math;


public class Vector2d {
	public static double ONEEIGHTYOVERPI = 180/Math.PI;
	public float X;
	public float Y;
	public float SPEED;
	public float SPEED_X;
	public float SPEED_Y;
	public float MOVE_ROTATION;
	public float LOOK_ROTATION;
	
	public Vector2d() {
		
	}
	
	public Vector2d(float x, float y) {
		this.X = x;
		this.Y = y;
	}
	
	public Vector2d(float x, float y, float speed, float speedX, float speedY, float moveRotation, float lookRotation) {
		this.X = x;
		this.Y = y;
		this.SPEED = speed;
		this.SPEED_X = speedX;
		this.SPEED_Y = speedY;
		this.MOVE_ROTATION = moveRotation;
		this.LOOK_ROTATION = lookRotation;
	}
	
	/*
	 * Get the distance between two Vector2D x and y coordinates
	 */
	public static double distance(Vector2d v1, Vector2d v2) {
		int xDiff = (int) Math.pow((v1.X-v2.X), 2);
		int yDiff = (int) Math.pow((v1.Y-v2.Y), 2);
		
		return Math.sqrt(xDiff + yDiff);
	}

	public static double distance(float x1, float x2, float y1, float y2) {
		int xDiff = (int) Math.pow((x1-x2), 2);
		int yDiff = (int) Math.pow((y2-y2), 2);

		return Math.sqrt(xDiff + yDiff);
	}
	
	/*
	 * Returns the angle(in degrees) between two sets of x and y coordinates on the 2D plane
	 */
	public static float getAngleBetweenTwoPoints(float x1, float x2, float y1, float y2)
	{
		double y = y2-y1;
		double x = x2-x1;
		
		float angle = (float) (Math.atan2(y, x) * ONEEIGHTYOVERPI);
		
		// This is here to make the returned number between 0 and 360
		if(angle < 0)
		{
			angle += 360;
		}
		// Added +90 because zero degrees is pointing up not right
		return (angle+90)%360;
	}
	
	/*
	 * Get dot product of two vectors
	 */
	public static int dot(Vector2d one, Vector2d two) {
		return (int)((one.X*two.X) + (one.Y*two.Y));
	}

	public float getLength()
	{
		return (float) Math.sqrt(X*X + Y*Y);
	}

	/*
	   * Returns the normalized vector of this Vector2D
	   */
	public Vector2d normalize()
	{
		return new Vector2d(X/getLength(), Y/getLength());
	}

	/*
	   * Normalize this Vector2d
	   */
	public void normalizeThis()
	{
		Vector2d v = normalize();

		X = v.X;
		Y = v.Y;
	}

	public static float getAngleBetweenTwoVectors(Vector2d one, Vector2d two)
	{
		one = one.normalize();
		two = two.normalize();

		return (float) (Math.atan2(one.X*two.Y - one.Y*two.X, one.X*two.X + one.Y*two.Y));
	}

}
