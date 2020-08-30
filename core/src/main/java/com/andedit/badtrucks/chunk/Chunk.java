package com.andedit.badtrucks.chunk;

import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.builders.TerrainBuilder;
import com.andedit.badtrucks.mesh.builders.TerrainBuilder.VertInfo;
import com.andedit.badtrucks.world.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.utils.Disposable;

public class Chunk implements Disposable
{
	private static final TerrainBuilder build = new TerrainBuilder();
	
	private static final VertInfo 
	v1 = new VertInfo(), 
	v2 = new VertInfo(), 
	v3 = new VertInfo(), 
	v4 = new VertInfo(),
	t1 = new VertInfo(), 
	t2 = new VertInfo(), 
	t3 = new VertInfo(), 
	t4 = new VertInfo();
	
	/** The size of the tiles. */
	public static final int SIZE = 32;
	public static final int MASK = SIZE-1;
	
	public static final int[] LOD = {1, 2, 4};
	
	public ChunkMesh[] meshes;
	
	public final int x, z;
	
	public float yMin, yMax, yCenter;
	
	private static final float green1 = Color.toFloatBits(63, 191, 63, 255);
	private static final float green2 = Color.toFloatBits(63, 153, 63, 255);
	private static final float red1 = Color.toFloatBits(191, 63, 63, 255);
	private static final float red2 = Color.toFloatBits(153, 63, 63, 255);
	
	//	v1----v4
	//	|      |
	//	|      |
	//	v2----v3
	public Chunk(World world, int xPos, int zPos) {
		this.x = xPos;
		this.z = zPos;
		meshes = new ChunkMesh[LOD.length];
		
		v1.c = v2.c = v3.c = v4.c = green1;
		for (int i = 0; i < LOD.length; i++)
		{
			build.begin(); // Clear all vertices for new mesh.
			final int lod = LOD[i]; // Caches LOD value.
			float min = 1024f, max = -1024f;
			for (int x = 0; x < SIZE; x += lod) {
				int realX = x+(xPos*SIZE);
			for (int z = 0; z < SIZE; z += lod) {
				int realZ = z+(zPos*SIZE);
				
				// Get height-maps.
				v1.y = world.getHeight(realX+lod, realZ);
				v2.y = world.getHeight(realX, realZ);
				v3.y = world.getHeight(realX, realZ+lod);
				v4.y = world.getHeight(realX+lod, realZ+lod);
				
				if (i == 0) { // Build a bounding box for frustum.
					max = Math.max(v2.y, max);
					min = Math.min(v2.y, min);
					if (z == MASK || x == MASK) {
						max = Math.max(v4.y, max);
						min = Math.min(v4.y, min);
					}
				}
				
				v1.x = realX+lod;
				v2.x = realX;
				v3.x = realX;
				v4.x = realX+lod;
				
				v1.z = realZ;
				v2.z = realZ;
				v3.z = realZ+lod;
				v4.z = realZ+lod;
				
				// Test colors.
				v1.c = v2.c = v3.c = v4.c = (((x+z)/lod)&1) == 1 ? green1 : green2;
				
				build.rect(v1, v2, v3, v4);
				
				if (i != 0) { // Filling gaps.
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
						
						t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
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
						
						t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
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
						
						t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
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
						
						t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
						build.rect(t1, t2, t3, t4);
					}
				}
			}} // End of mesh building.
			if (i == 0) {
				yMin = min;
				yMax = max;
				yCenter = (min+max)/2f;
			}
			meshes[i] = build.create();
		}
	}
	
	public boolean frust(Plane[] planes) {
		for (Plane plane : planes) {
			if (testBounds(plane) != PlaneSide.Back) {
				continue;
			}
			return false;
		}
		
		return true;
	}
	
	public void render(Vector3 camPos) {
		final int haft = (SIZE/2);
		//meshes[2].render();
		meshes[Math.min(LOD.length-1, (int)(camPos.dst((x*SIZE)+haft, 0f, (z*SIZE)+haft)*0.01f))].render(); // 0.01f
	}

	@Override
	public void dispose() {
		for (int i = 0; i < LOD.length; i++)
			meshes[i].dispose();
	}
	
	// Taken from https://github.com/libgdx/libgdx/pull/6086
	private PlaneSide testBounds(final Plane plane) { 
		final float size = SIZE;
		// Compute the projection interval radius of b onto L(t) = b.c + t * p.n
		final float radius = size * Math.abs(plane.normal.x) +
					  (yMax-yMin) * Math.abs(plane.normal.y) +
					   		 size * Math.abs(plane.normal.z);

		// Compute distance of box center from plane
		final float dist = plane.normal.dot(x*SIZE, yCenter, z*SIZE) + plane.d;

		// Intersection occurs when plane distance falls within [-r,+r] interval
		if (dist > radius) {
			return PlaneSide.Front;
		} else if (dist < -radius) {
			return PlaneSide.Back;
		}
		
		return PlaneSide.OnPlane;
	}
}
