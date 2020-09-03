package com.andedit.badtrucks.chunk;

import static com.badlogic.gdx.math.Interpolation.circleOut;

import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.builders.TerrainBuilder;
import com.andedit.badtrucks.mesh.builders.TerrainBuilder.VertInfo;
import com.andedit.badtrucks.world.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Interpolation.ExpOut;
import com.badlogic.gdx.math.MathUtils;
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
	
	public float yMin, yMax, yCenter, haftHeight;
	
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
		
		//v1.c = v2.c = v3.c = v4.c = green1;
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
				v1.yPos = world.getHeight(realX+lod, realZ);
				v2.yPos = world.getHeight(realX, realZ);
				v3.yPos = world.getHeight(realX, realZ+lod);
				v4.yPos = world.getHeight(realX+lod, realZ+lod);
				
				if (i == 0) { // Build a bounding box for frustum.
					max = Math.max(v2.yPos, max);
					min = Math.min(v2.yPos, min);
					max = Math.max(v4.yPos, max);
					min = Math.min(v4.yPos, min);
				}
				
				v1.xPos = realX+lod;
				v2.xPos = realX;
				v3.xPos = realX;
				v4.xPos = realX+lod;
				
				v1.zPos = realZ;
				v2.zPos = realZ;
				v3.zPos = realZ+lod;
				v4.zPos = realZ+lod;
				
				// Test colors.
				//v1.c = v2.c = v3.c = v4.c = (((x+z)/lod)&1) == 1 ? green1 : green2;
				
				build.rect(v1, v2, v3, v4);
				
				if (i != 0) { // Filling gaps.
					final float offset = 0.2f*lod;
					if (x == 0 && xPos != 0 && ((v2.yPos+v3.yPos)/2f)>world.getHeight(realX, realZ+lod-(lod/2))) {
						t1.set(v1); t1.xPos -= lod;
						t2.set(v2); t2.xPos -= offset;
						t3.set(v3); t3.xPos -= offset;
						t4.set(v4); t4.xPos -= lod;
						t1.yPos = t2.yPos;
						t4.yPos = t3.yPos;
						
						t2.yPos = world.getHeightLerp(t2.xPos, t2.zPos)-offset;
						t3.yPos = world.getHeightLerp(t3.xPos, t3.zPos)-offset;
						
						//t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
						build.rect(t1, t2, t3, t4);
					} else if ((x+lod+lod) > SIZE && xPos != World.MASK && ((v1.yPos+v4.yPos)/2f)>world.getHeight(realX+lod, realZ+lod-(lod/2))) {
						t1.set(v1); t1.xPos += offset;
						t2.set(v2); t2.xPos += lod;
						t3.set(v3); t3.xPos += lod;
						t4.set(v4); t4.xPos += offset;
						t2.yPos = t1.yPos;
						t3.yPos = t4.yPos;
						
						t1.yPos = world.getHeightLerp(t1.xPos, t1.zPos)-offset;
						t4.yPos = world.getHeightLerp(t4.xPos, t4.zPos)-offset;
						
						//t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
						build.rect(t1, t2, t3, t4);
					}
					if (z == 0 && zPos != 0 && ((v1.yPos+v2.yPos)/2f)>world.getHeight(realX+lod-(lod/2), realZ)) {
						t1.set(v1); t1.zPos -= offset;
						t2.set(v2); t2.zPos -= offset;
						t3.set(v3); t3.zPos -= lod;
						t4.set(v4); t4.zPos -= lod;
						t3.yPos = t2.yPos;
						t4.yPos = t1.yPos;
						
						t1.yPos = world.getHeightLerp(t1.xPos, t1.zPos)-offset;
						t2.yPos = world.getHeightLerp(t2.xPos, t2.zPos)-offset;
						
						//t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
						build.rect(t1, t2, t3, t4);
					} else if ((z+lod+lod) > SIZE && zPos != World.MASK && ((v3.yPos+v4.yPos)/2f)>world.getHeight(realX+lod-(lod/2), realZ+lod)) {
						t1.set(v1); t1.zPos += lod;
						t2.set(v2); t2.zPos += lod;
						t3.set(v3); t3.zPos += offset;
						t4.set(v4); t4.zPos += offset;
						t2.yPos = t3.yPos;
						t1.yPos = t4.yPos;
						
						t4.yPos = world.getHeightLerp(t4.xPos, t4.zPos)-offset;
						t3.yPos = world.getHeightLerp(t3.xPos, t3.zPos)-offset;
						
						//t1.c = t2.c = t3.c = t4.c = (((x+z)/lod)&1) == 0 ? red1 : red2;
						build.rect(t1, t2, t3, t4);
					}
				}
			}} // End of mesh building.
			if (i == 0) {
				yMin = min;
				yMax = max;
				yCenter = (min+max)/2f;
				haftHeight = (max-min)/2f;
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
		final float dst = camPos.dst((x*SIZE)+haft, yCenter, (z*SIZE)+haft)*(0.01f/2.3f); // 0.01f
		meshes[Math.min(LOD.length-1, (int)exp.apply(0f, LOD.length-1, dst))].render();
	}

	@Override
	public void dispose() {
		for (int i = 0; i < LOD.length; i++)
			meshes[i].dispose();
	}
	
	// Taken from https://github.com/libgdx/libgdx/pull/6086
	private PlaneSide testBounds(final Plane plane) { 
		final float haft = SIZE/2;
		// Compute the projection interval radius of b onto L(t) = b.c + t * p.n
		final float radius = haft * Math.abs(plane.normal.x) +
							 haftHeight * Math.abs(plane.normal.y) +
							 haft * Math.abs(plane.normal.z);

		// Compute distance of box center from plane
		final float dist = plane.normal.dot((x*SIZE)+haft, yCenter, (z*SIZE)+haft) + plane.d;

		// Intersection occurs when plane distance falls within [-r,+r] interval
		if (dist > radius) {
			return PlaneSide.Front;
		} else if (dist < -radius) {
			return PlaneSide.Back;
		}
		
		return PlaneSide.OnPlane;
	}
	
	private static final Vector3 temNor = new Vector3();
	private static Vector3 calcNormals(World world, int x, int z) {
		return temNor.set(world.getHeight(x-1, z)-world.getHeight(x+1, z), 2f, 
						  world.getHeight(x, z-1)-world.getHeight(x, z+1)).nor();
	}
	
	private static final ExpOut exp = new ExpOut(2, 2.2f);
}
