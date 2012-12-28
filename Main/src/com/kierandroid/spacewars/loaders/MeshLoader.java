package com.kierandroid.spacewars.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.kierandroid.spacewars.Utilities.MeshLoaderResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MeshLoader extends AsynchronousAssetLoader<MeshLoaderResult, MeshLoader.MeshParameter>
{
	private MeshLoaderResult result;

	public MeshLoader(InternalFileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager assetManager, String s, MeshParameter meshParameter) {
		result = null;

		boolean  useIndices = false;
		boolean flipV = true;
		String obj = "";

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.internal(s).read()));
			StringBuffer b = new StringBuffer();
			String l = reader.readLine();
			while (l != null) {
				b.append(l);
				b.append("\n");
				l = reader.readLine();
			}

			obj = b.toString();
			reader.close();
		} catch (Exception ex) {
			return;
		}

		String[] lines = obj.split("\n");
		float[] vertices = new float[lines.length * 3];
		float[] normals = new float[lines.length * 3];
		float[] uv = new float[lines.length * 3];

		int numVertices = 0;
		int numNormals = 0;
		int numUV = 0;
		int numFaces = 0;

		int[] facesVerts = new int[lines.length * 3];
		int[] facesNormals = new int[lines.length * 3];
		int[] facesUV = new int[lines.length * 3];
		int vertexIndex = 0;
		int normalIndex = 0;
		int uvIndex = 0;
		int faceIndex = 0;

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("v ")) {
				String[] tokens = line.split("[ ]+");
				vertices[vertexIndex] = Float.parseFloat(tokens[1]);
				vertices[vertexIndex + 1] = Float.parseFloat(tokens[2]);
				vertices[vertexIndex + 2] = Float.parseFloat(tokens[3]);
				vertexIndex += 3;
				numVertices++;
				continue;
			}

			if (line.startsWith("vn ")) {
				String[] tokens = line.split("[ ]+");
				normals[normalIndex] = Float.parseFloat(tokens[1]);
				normals[normalIndex + 1] = Float.parseFloat(tokens[2]);
				normals[normalIndex + 2] = Float.parseFloat(tokens[3]);
				normalIndex += 3;
				numNormals++;
				continue;
			}

			if (line.startsWith("vt")) {
				String[] tokens = line.split("[ ]+");
				uv[uvIndex] = Float.parseFloat(tokens[1]);
				uv[uvIndex + 1] = flipV ? 1 - Float.parseFloat(tokens[2]) : Float.parseFloat(tokens[2]);
				uvIndex += 2;
				numUV++;
				continue;
			}

			if (line.startsWith("f ")) {
				String[] tokens = line.split("[ ]+");

				String[] parts = tokens[1].split("/");
				facesVerts[faceIndex] = getIndex(parts[0], numVertices);
				if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
				if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
				faceIndex++;

				parts = tokens[2].split("/");
				facesVerts[faceIndex] = getIndex(parts[0], numVertices);
				if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
				if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
				faceIndex++;

				parts = tokens[3].split("/");
				facesVerts[faceIndex] = getIndex(parts[0], numVertices);
				if (parts.length > 2) facesNormals[faceIndex] = getIndex(parts[2], numNormals);
				if (parts.length > 1) facesUV[faceIndex] = getIndex(parts[1], numUV);
				faceIndex++;
				numFaces++;
				continue;
			}
		}

		ArrayList<VertexAttribute> attributes = new ArrayList<VertexAttribute>();
		attributes.add(new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
		if (numNormals > 0) attributes.add(new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		if (numUV > 0) attributes.add(new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

		float[] verts = new float[(numFaces * 3) * (3 + (numNormals > 0 ? 3 : 0) + (numUV > 0 ? 2 : 0))];

		for (int i = 0, vi = 0; i < numFaces * 3; i++) {
			int vertexIdx = facesVerts[i] * 3;
			verts[vi++] = vertices[vertexIdx];
			verts[vi++] = vertices[vertexIdx + 1];
			verts[vi++] = vertices[vertexIdx + 2];

			if (numNormals > 0) {
				int normalIdx = facesNormals[i] * 3;
				verts[vi++] = normals[normalIdx];
				verts[vi++] = normals[normalIdx + 1];
				verts[vi++] = normals[normalIdx + 2];
			}
			if (numUV > 0) {
				int uvIdx = facesUV[i] * 2;
				verts[vi++] = uv[uvIdx];
				verts[vi++] = uv[uvIdx + 1];
			}
		}

		result = new MeshLoaderResult(attributes, verts, numFaces);
		//mesh = new Mesh(true, numFaces * 3, 0, attributes.toArray(new VertexAttribute[attributes.size()]));
		//mesh.setVertices(verts);
	}

	private static int getIndex (String index, int size) {
		if (index == null || index.length() == 0) return 0;
		int idx = Integer.parseInt(index);
		if (idx < 0)
			return size + idx;
		else
			return idx - 1;
	}

	@Override
	public MeshLoaderResult loadSync(AssetManager assetManager, String s, MeshParameter meshParameter) {
		return result;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String s, MeshParameter meshParameter) {
		return null;
	}

	public static class MeshParameter extends AssetLoaderParameters<MeshLoaderResult>
	{

	}
}
