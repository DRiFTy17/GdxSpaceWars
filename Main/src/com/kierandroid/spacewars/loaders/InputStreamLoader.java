package com.kierandroid.spacewars.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;

import java.io.InputStream;

public class InputStreamLoader extends AsynchronousAssetLoader<InputStream, InputStreamLoader.Parameter>
{
	private InputStream is;

	public InputStreamLoader(InternalFileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager assetManager, String fileName, Parameter parameter) {
		is = null;
		is = Gdx.files.internal(fileName).read();
	}

	@Override
	public InputStream loadSync(AssetManager assetManager, String fileName, Parameter parameter) {
		return is;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String s, Parameter parameter) {
		return null;
	}


	public static class Parameter extends AssetLoaderParameters<InputStream>
	{

	}
}
