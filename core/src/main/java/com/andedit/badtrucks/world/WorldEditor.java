package com.andedit.badtrucks.world;

import static com.badlogic.gdx.math.Interpolation.smooth;
import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.andedit.badtrucks.chunk.Chunk;
import com.andedit.badtrucks.chunk.ChunkStream;
import com.andedit.badtrucks.utils.Util;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.NumberUtils;

public class WorldEditor extends World 
{	
	public final int[][] dat;

	public WorldEditor() {
		super(false, true);
		dat = new int[LENGTH][LENGTH];
		for (int x = 0; x < SIZE; x++)
		for (int z = 0; z < SIZE; z++) {
			chunks[x][z] = new ChunkStream(this, x, z);
		}
	}
	
	/** Get data. */
	public int getData(int x, int z) {
		return dat[clamp(x, 0, MASKLEN)][clamp(z, 0, MASKLEN)]&0xFFFFFF;
	}
	
	// {path, gravel}
	private static final int[] 
	andIndex   = {0xFFFFFF00, 0xFFFF00FF},
	shiftIndex = {0, 8};
	private static final int IndexLen = shiftIndex.length;
	
	/** Get data. */
	public int getData(int x, int z, int index) {
		return index == -1 ? 0 : (dat[clamp(x, 0, MASKLEN)][clamp(z, 0, MASKLEN)]&(~andIndex[index]))>>>shiftIndex[index];
	}
	
	/** Set data. */
	public void setData(int x, int z, int value, int index) {
		if (x < 0 || x >= LENGTH || z < 0 || z >= LENGTH)
			return;
		value = clamp(value, 0, 255);
		for (int i = 0; i < IndexLen; i++) {
			int raw = getData(x, z, i);
			dat[x][z] = (dat[x][z]&andIndex[i])|(Math.min(raw, 255-value)<<shiftIndex[i]);
		}
		if (index != -1) {
			dat[x][z] = (dat[x][z]&andIndex[index])|(value<<shiftIndex[index]);
		}
		
	}
	
	/** Set data. */
	public void setOthersData(int x, int z, int value, int notIndex) {
		if (x < 0 || x >= LENGTH || z < 0 || z >= LENGTH)
			return;
		for (int i = 0; i < andIndex.length; i++) {
			if (notIndex == -1 || notIndex != i) {
				
			}
		}
		dat[x][z] = 0;
	}
	
	/** Set data. */
	public void clearData(int x, int z) {
		if (x < 0 || x >= LENGTH || z < 0 || z >= LENGTH)
			return;
		dat[x][z] = 0;
	}
	
	/** Get data. */
	public float packData(int x, int z) {
		return NumberUtils.intToFloatColor(getData(x, z)|(int)(255*calcLight(x, z))<<24);
	}
	
	private static final float LENGHT = 512.0f;
	private static final float STEPS = 0.1f;
	private static final Vector3 pos = new Vector3();
	private static final Vector3 nor = new Vector3();
	
	/** Raycast height-map. 
	 * @param lastHeight Float.NaN for no last height. */
	public Vector3 raycast(Vector3 camPos, Vector3 dir, float lastHeight) {
		final boolean isNaN = Float.isNaN(lastHeight);
		nor.set(dir).scl(STEPS);
		pos.set(camPos);
		
		for (float i = 0; i < LENGHT; i += STEPS) {
			pos.add(nor);
			if (isNaN) {
				if (getHeightRaw(pos.x, pos.z) > pos.y)
					return pos;
			} else {
				if (lastHeight > pos.y)
					return pos;
			}
		}
		
		return null;
	}
	
	public void bumpHeights(float xPos, float zPos, float minSize, float maxSize, float value) {
		final float fSize = maxSize*0.5f;
		final int iSize = (int)(fSize+1f);	
		final int xInt = floor(xPos), zInt = floor(zPos);
		minSize = 1f-(minSize/maxSize);
		
		for (int x = -iSize; x < iSize; x++)
		for (int z = -iSize; z < iSize; z++) {
			float sqrt = (1f-(Util.sqrt(x*x+z*z)/fSize))/minSize;
			
			if (sqrt > 0f) {
				if (sqrt > 1.0f) {
					addHeight(x+xInt, z+zInt, 1.0f*value);
				} else {
					addHeight(x+xInt, z+zInt, smooth.apply(sqrt)*value);
				}
			}
			
			int xChunk = (x+xInt)/Chunk.SIZE;
			int zChunk = (z+zInt)/Chunk.SIZE;
			if (xChunk < 0 || xChunk >= SIZE || zChunk < 0 || zChunk >=  SIZE) continue;
			chunks[xChunk][zChunk].isDirty = true;
		}
	}
	
	public void paint(float xPos, float zPos, float minSize, float maxSize, float value, int index) {
		final float fSize = maxSize*0.5f;
		final int iSize = (int)(fSize+1f);	
		final int xInt = floor(xPos), zInt = floor(zPos);
		minSize = 1f-(minSize/maxSize);
		
		value *= 255f;
		
		for (int x = -iSize; x < iSize; x++)
		for (int z = -iSize; z < iSize; z++) {
			float sqrt = (1f-(Util.sqrt(x*x+z*z)/fSize))/minSize;
			
			if (sqrt > 0f) {
				if (sqrt > 1.0f) {
					setData(x+xInt, z+zInt, (int)value, index);
				} else {
					final int raw = Math.max(getData(x+xInt, z+zInt, index), (int)(smooth.apply(sqrt)*value));
					setData(x+xInt, z+zInt, raw, index);
				}
			}
			
			int xChunk = (x+xInt)/Chunk.SIZE;
			int zChunk = (z+zInt)/Chunk.SIZE;
			if (xChunk < 0 || xChunk >= SIZE || zChunk < 0 || zChunk >=  SIZE) continue;
			chunks[xChunk][zChunk].isDirty = true;
		}
	}
}
