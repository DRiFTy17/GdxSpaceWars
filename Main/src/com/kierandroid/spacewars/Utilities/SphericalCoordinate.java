package com.kierandroid.spacewars.Utilities;

public class SphericalCoordinate {
	private float rho;
	private float theta;
	private float phi;

	public SphericalCoordinate(float rho)
	{
		this.rho = rho;
		this.theta = 0.0f;
		this.phi = 0.0f;
	}

	public SphericalCoordinate(float rho, float theta, float phi)
	{
		this(rho);

		this.theta = theta;
		this.phi = phi;
	}

	public float getRadius()
	{
		return rho;
	}

	public float getLatitude()
	{
		return theta;
	}

	public float getLongitude()
	{
		return phi;
	}

	public void setRadius(float value)
	{
		rho = value;
	}

	public void setLatitude(float value)
	{
		theta = value;
	}

	public void setLongitude(float value)
	{
		phi = value;
	}

}
