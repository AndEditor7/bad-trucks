package com.andedit.badtrucks.world;

import static com.andedit.badtrucks.handles.Shaders.shader;
import static com.badlogic.gdx.math.Interpolation.smooth;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.andedit.badtrucks.chunk.Chunk;
import com.andedit.badtrucks.handles.Shaders;
import com.andedit.badtrucks.utils.Camera;
import com.andedit.badtrucks.utils.FastNoise;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class World implements Disposable
{
	/** The size of the chunks */
	public static final int SIZE = 16;
	public static final int LENGTH = SIZE*Chunk.SIZE;
	
	public final Chunk[][] chunks;
	public final float[][] map;
	
	public World() {
		this.chunks = new Chunk[SIZE][SIZE];
		this.map = new float[LENGTH][LENGTH];
		
		for (int x = 0; x < LENGTH; x++)
		for (int z = 0; z < LENGTH; z++) {
			map[x][z] = FastNoise.getPerlin(1337, 0.1f*x, 0.1f*z)*8f;
		}
		
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z] = new Chunk(this, x, z);
		}
	}
	
	public void upload() {
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z].upload();
		}
	}
	
	public void render(Camera cam) {
		final Vector3 camPos = cam.position;
		Shaders.bind(cam.combined);
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z].render(camPos);
		}
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

	@Override
	public void dispose() {
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z].dispose();
		}	
	}
}
