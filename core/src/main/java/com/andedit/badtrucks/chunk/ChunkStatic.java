package com.andedit.badtrucks.chunk;

import com.andedit.badtrucks.mesh.builders.TerrainStaticBuilder;
import com.andedit.badtrucks.world.World;
import com.badlogic.gdx.math.Vector3;

public class ChunkStatic extends Chunk 
{
	private static final TerrainStaticBuilder build = new TerrainStaticBuilder();
	
	private static final int[] LOD = {1, 2, 4};
	
	protected static final Vector3 
	v1 = new Vector3(), 
	v2 = new Vector3(), 
	v3 = new Vector3(), 
	v4 = new Vector3(),
	t1 = new Vector3(), 
	t2 = new Vector3(), 
	t3 = new Vector3(), 
	t4 = new Vector3();

	public ChunkStatic(World world, int xPos, int zPos) {
		super(world, xPos, zPos, LOD.length);
	}

	protected void build(final int indexLOD, final boolean isModify, final boolean clearOthers) {
		build.begin(); // Clear all vertices for new mesh.
		float min = 1024f, max = -1024f;
		final int lod = LOD[indexLOD];
		for (int x = 0; x < SIZE; x += lod) {
			int realX = x+(xPos*SIZE);
		for (int z = 0; z < SIZE; z += lod) {
			int realZ = z+(zPos*SIZE);
			
			// Get height-maps.
			v1.y = world.getHeight(realX+lod, realZ);
			v2.y = world.getHeight(realX, realZ);
			v3.y = world.getHeight(realX, realZ+lod);
			v4.y = world.getHeight(realX+lod, realZ+lod);
			
			// Build a bounding box for frustum.
			if (isModify || indexLOD == 0) { 
				max = Math.max(v2.y, max);
				min = Math.min(v2.y, min);
				max = Math.max(v4.y, max);
				min = Math.min(v4.y, min);
			}
			
			v1.x = realX+lod;
			v2.x = realX;
			v3.x = realX;
			v4.x = realX+lod;
			
			v1.z = realZ;
			v2.z = realZ;
			v3.z = realZ+lod;
			v4.z = realZ+lod;
			
			build.rect(v1, v2, v3, v4);
			
			if (indexLOD != 0) { // Filling gaps.
				final float offset = 0.2f*lod;
				if (x == 0 && xPos != 0 && ((v2.y+v3.y)/2f)>world.getHeight(realX, realZ+lod-(lod/2))) {
					t1.set(v1); t1.x -= lod;
					t2.set(v2); t2.x -= offset;
					t3.set(v3); t3.x -= offset;
					t4.set(v4); t4.x -= lod;
					t1.y = t2.y;
					t4.y = t3.y;
					
					t2.y = world.getHeightLerp(t2.x, t2.z)-offset;
					t3.y = world.getHeightLerp(t3.x, t3.z)-offset;
					
					build.rect(t1, t2, t3, t4);
				} else if ((x+lod+lod) > SIZE && xPos != World.MASK && ((v1.y+v4.y)/2f)>world.getHeight(realX+lod, realZ+lod-(lod/2))) {
					t1.set(v1); t1.x += offset;
					t2.set(v2); t2.x += lod;
					t3.set(v3); t3.x += lod;
					t4.set(v4); t4.x += offset;
					t2.y = t1.y;
					t3.y = t4.y;
					
					t1.y = world.getHeightLerp(t1.x, t1.z)-offset;
					t4.y = world.getHeightLerp(t4.x, t4.z)-offset;
					
					build.rect(t1, t2, t3, t4);
				}
				if (z == 0 && zPos != 0 && ((v1.y+v2.y)/2f)>world.getHeight(realX+lod-(lod/2), realZ)) {
					t1.set(v1); t1.z -= offset;
					t2.set(v2); t2.z -= offset;
					t3.set(v3); t3.z -= lod;
					t4.set(v4); t4.z -= lod;
					t3.y = t2.y;
					t4.y = t1.y;
					
					t1.y = world.getHeightLerp(t1.x, t1.z)-offset;
					t2.y = world.getHeightLerp(t2.x, t2.z)-offset;
					
					build.rect(t1, t2, t3, t4);
				} else if ((z+lod+lod) > SIZE && zPos != World.MASK && ((v3.y+v4.y)/2f)>world.getHeight(realX+lod-(lod/2), realZ+lod)) {
					t1.set(v1); t1.z += lod;
					t2.set(v2); t2.z += lod;
					t3.set(v3); t3.z += offset;
					t4.set(v4); t4.z += offset;
					t2.y = t3.y;
					t1.y = t4.y;
					
					t4.y = world.getHeightLerp(t4.x, t4.z)-offset;
					t3.y = world.getHeightLerp(t3.x, t3.z)-offset;
					
					build.rect(t1, t2, t3, t4);
				}
			}
		}}
		
		if (isModify || indexLOD == 0) {
			yMin = min;
			yMax = max;
			yCenter = (min+max)/2f;
			haftHeight = (max-min)/2f;
		}
		
		if (isModify) {
			if (clearOthers)
			for (int i = 0; i < meshes.length; i++) {
				if (i == indexLOD) continue;
				meshes[i].isDirty = true;
			}
			meshes[indexLOD].setVertices(build.vertices);
			meshes[indexLOD].isDirty = false;
		} else {
			meshes[indexLOD] = build.create();
		}
	}
}
