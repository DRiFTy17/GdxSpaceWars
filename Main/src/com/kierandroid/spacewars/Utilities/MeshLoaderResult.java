package com.kierandroid.spacewars.Utilities;

import com.badlogic.gdx.graphics.VertexAttribute;

import java.util.ArrayList;

public class MeshLoaderResult
{
	public ArrayList<VertexAttribute> attributes;
	public float[] verts;
	public int numFaces;

	public MeshLoaderResult(ArrayList<VertexAttribute> attributes, float[] verts, int numFaces)
	{
		this.attributes = attributes;
		this.verts = verts;
		this.numFaces = numFaces;
	}
}
