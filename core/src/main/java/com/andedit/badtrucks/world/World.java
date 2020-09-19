package com.andedit.badtrucks.world;

import static com.badlogic.gdx.math.Interpolation.smooth;
import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.clamp;

import java.util.Random;

import com.andedit.badtrucks.chunk.Chunk;
import com.andedit.badtrucks.chunk.ChunkStatic;
import com.andedit.badtrucks.mesh.verts.TerrainStatic;
import com.andedit.badtrucks.mesh.verts.TerrainStream;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.FastNoise;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class World implements Disposable
{
	/** The size of the chunks */
	public static final int SIZE = 16;
	public static final int LENGTH = SIZE*Chunk.SIZE;
	public static final int MASK = SIZE-1;
	public static final int MASKLEN = LENGTH-1;
	
	public final Chunk[][] chunks;
	public final float[][] map;
	
	public final IndexData indices;
	public final boolean isEditor;
	
	public World(boolean noise) {
		this(noise, false);
	}
	
	protected World(boolean noise, boolean isEditor) {
		this.chunks = new Chunk[SIZE][SIZE];
		this.map = new float[LENGTH][LENGTH];
		this.isEditor = isEditor;
		
		if (noise) {
			final Random rand = MathUtils.random;
			final int 
			seed1 = rand.nextInt(), 
			seed2 = rand.nextInt(),
			seed3 = rand.nextInt();
			for (int x = 0; x < LENGTH; x++)
			for (int z = 0; z < LENGTH; z++) {
				float value;
				value =  FastNoise.getPerlin(seed1, 0.04f*x, 0.04f*z)*8f;
				value += FastNoise.getPerlin(seed2, 0.02f*x, 0.02f*z)*16f;
				value += FastNoise.getPerlin(seed3, 0.01f*x, 0.01f*z)*32f;
				map[x][z] = value;
			}
		}
		
		if (!isEditor)
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z] = new ChunkStatic(this, x, z);
		}
		
		final int len = 98304>>1;
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
		
		indices.bind();
		if (isEditor) TerrainStream.bind(cam);
		else TerrainStatic.bind(cam);
		for(int x = 0; x < SIZE; x++)
		for(int z = 0; z < SIZE; z++) {
			Chunk chunk = chunks[x][z];
			if (chunk.frust(planes))
				chunk.render(camPos);
		}
		indices.unbind();
	}
	
	/** Get height-map. */
	public float getHeight(int x, int z) {
		return map[clamp(x, 0, MASKLEN)][clamp(z, 0, MASKLEN)];
	}
	
	/** Get height-map. */
	public void addHeight(int x, int z, float value) {
		if (x < 0 || x >= LENGTH || z < 0 || z >= LENGTH)
			return;
		map[x][z] += value;
	}
	
	/** Get height-map without interpolation. */
	public float getHeightRaw(float x, float z) {
		final int xInt, zInt;
		if ((xInt = floor(x)) < 0 || xInt >= LENGTH || (zInt = floor(z)) < 0 || zInt >= LENGTH)
			return 0f;
		
		return map[xInt][zInt];
	}
	
	/** Get height-map with smoothstep interpolation. */
    public float getHeightSmooth(float x, float z) {
        final int xInt = floor(x), zInt = floor(z);
        return smooth.apply(smooth.apply(getHeight(xInt, zInt  ), getHeight(xInt+1, zInt  ), x - xInt), 
        					smooth.apply(getHeight(xInt, zInt+1), getHeight(xInt+1, zInt+1), x - xInt), z - zInt);

        //interpolate(interpolate(lowerLeft, lowerRight, xFraction), interpolate(upperLeft, upperRight, xFraction), yFraction);
    }
    
    /** Get height-map with linear interpolation. */
    public float getHeightLerp(float x, float z) {
        final int xInt = (int) Math.floor(x), zInt = floor(z);
        return MathUtils.lerp(MathUtils.lerp(getHeight(xInt, zInt  ), getHeight(xInt+1, zInt  ), x - xInt), 
        					  MathUtils.lerp(getHeight(xInt, zInt+1), getHeight(xInt+1, zInt+1), x - xInt), z - zInt);

        //interpolate(interpolate(lowerLeft, lowerRight, xFraction), interpolate(upperLeft, upperRight, xFraction), yFraction);
    }
    
    // calculate light.
    private static final Vector3 vec = new Vector3();
    private static final Vector3 dir = new Quaternion().setEulerAngles(30, -73, 0).transform(new Vector3(0,0,1));
	public float calcLight(int x, int z) {
		final float dot;
		dot = vec.set(getHeight(x-1, z)-getHeight(x+1, z), 2f, 
					  getHeight(x, z-1)-getHeight(x, z+1)).nor().dot(dir);
		return MathUtils.lerp(Math.max(dot, 0.2f), 1.0f, 0.25f);
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
