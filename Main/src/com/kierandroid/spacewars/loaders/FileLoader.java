package com.kierandroid.spacewars.loaders;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileLoader extends AsynchronousAssetLoader<String, FileLoader.StringParameter>
{
	String contents;

	public FileLoader(InternalFileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager assetManager, String s, StringParameter stringParameter) {
		contents = null;
		contents = "";

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.internal(s).read()));
			StringBuffer b = new StringBuffer();
			String l = reader.readLine();
			while (l != null) {
				b.append(l);
				b.append("\n");
				l = reader.readLine();
			}

			contents = b.toString();
			reader.close();
		} catch (Exception ex) {
			contents = null;
		}
	}

	@Override
	public String loadSync(AssetManager assetManager, String s, StringParameter stringParameter) {
		return contents;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String s, StringParameter stringParameter) {
		return null;
	}

	public static class StringParameter extends AssetLoaderParameters<String>
	{
	}
}
