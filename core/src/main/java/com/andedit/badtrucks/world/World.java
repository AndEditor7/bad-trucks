package com.andedit.badtrucks.world;

import static com.badlogic.gdx.math.Interpolation.smooth;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.andedit.badtrucks.chunk.Chunk;
import com.andedit.badtrucks.handles.Shaders;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.FastNoise;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class World implements Disposable
{
	/** The size of the chunks */
	public static final int SIZE = 16;
	public static final int LENGTH = SIZE*Chunk.SIZE;
	public static final int MASK = SIZE-1;
	
	public final Chunk[][] chunks;
	public final float[][] map;
	
	public IndexData indices;
	
	public World() {
		this.chunks = new Chunk[SIZE][SIZE];
		this.map = new float[LENGTH][LENGTH];
		
		for (int x = 0; x < LENGTH; x++)
		for (int z = 0; z < LENGTH; z++) {
			//map[x][z] =  FastNoise.getPerlin(1337, 0.06f*x, 0.06f*z)*7f; // 7f
			//map[x][z] += FastNoise.getPerlin(2345, 0.02f*x, 0.02f*z)*20f; // 20f
			map[x][z] =  FastNoise.getPerlin(1337, x/6f, z/6f)*8f; // 7f
			map[x][z] += FastNoise.getPerlin(2345, x/12f, z/12f)*16f; // 20f
		}
		
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z] = new Chunk(this, x, z);
		}
		
		final int len = 98304/2;
		final short[] index = new short[len];
		for (int i = 0, v = 0; i < len; i += 6, v += 4) {
			index[i] = (short)v;
			index[i+1] = (short)(v+1);
			index[i+2] = (short)(v+2);
			index[i+3] = (short)(v+2);
			index[i+4] = (short)(v+3);
			index[i+5] = (short)v;
		}
		
		indices = new IndexBufferObject(true, len);
		indices.setIndices(index, 0, len);
	}
	
	public void render(Camera cam) {
		final Vector3 camPos = cam.position;
		final Plane[] planes = cam.frustum.planes;
		
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		indices.bind();
		Shaders.bind(cam.combined);
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			Chunk chunk = chunks[x][z];
			if (chunk.frust(planes))
			chunks[x][z].render(camPos);
		}
		indices.unbind();
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
	}
	
	/** Get height-map. */
	public float getHeight(int x, int z) {
		if (x < 0 || x >= LENGTH || z < 0 || z >= LENGTH)
			return 0f;
		
		return map[x][z];
	}
	
	/** Get height-map without interpolation. */
	public float getHeightRaw(float x, float z) {
		final int xInt, zInt;
		if ((xInt = floor(x)) < 0 || xInt >= LENGTH || (zInt = floor(z)) < 0 || zInt >= LENGTH)
			return 0f;
		
		return map[xInt][zInt];
	}
	
	/** Get height-map with interpolation. */
    public float getHeightSmooth(float x, float z) {
        final int xInt = floor(x), zInt = floor(z);
        return smooth.apply(smooth.apply(getHeight(xInt, zInt  ), getHeight(xInt+1, zInt  ), x - xInt), 
        					smooth.apply(getHeight(xInt, zInt+1), getHeight(xInt+1, zInt+1), x - xInt), z - zInt);

        //interpolate(interpolate(lowerLeft, lowerRight, xFraction), interpolate(upperLeft, upperRight, xFraction), yFraction);
    }
    
    /** Get height-map with lerp. */
    public float getHeightLerp(float x, float z) {
        final int xInt = floor(x), zInt = floor(z);
        return MathUtils.lerp(MathUtils.lerp(getHeight(xInt, zInt  ), getHeight(xInt+1, zInt  ), x - xInt), 
        					  MathUtils.lerp(getHeight(xInt, zInt+1), getHeight(xInt+1, zInt+1), x - xInt), z - zInt);

        //interpolate(interpolate(lowerLeft, lowerRight, xFraction), interpolate(upperLeft, upperRight, xFraction), yFraction);
    }

	@Override
	public void dispose() {
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z].dispose();
		}
		indices.dispose();
	}
}
