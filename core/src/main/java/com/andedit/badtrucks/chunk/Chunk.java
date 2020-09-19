package com.andedit.badtrucks.chunk;

import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.world.World;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane.PlaneSide;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.utils.Disposable;

public abstract class Chunk implements Disposable
{
	/** The size of the tiles. */
	public static final int SIZE = 32;
	public static final int MASK = SIZE-1;
	
	protected final ChunkMesh[] meshes;
	protected final int len;
	
	public final int xPos, zPos;
	public float yMin, yMax, yCenter, haftHeight;
	
	public boolean isDirty;
	
	protected final World world;
	
	//	v1----v4
	//	|      |
	//	|      |
	//	v2----v3
	public Chunk(World world, int xPos, int zPos, int len) {
		this.world = world;
		this.xPos = xPos;
		this.zPos = zPos;
		this.len = len;
		meshes = new ChunkMesh[len];
		
		for (int i = 0; i < len; i++) {
			build(i, false, false);
		}
	}
	
	/** 
	 * Build the chunk mesh
	 * */
	protected abstract void build(int indexLOD, boolean isModify, boolean clearOthers);
	
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
		final float dst = camPos.dst((xPos*SIZE)+haft, yCenter, (zPos*SIZE)+haft)/256f; // 0.01f
		final int index = Math.min(len-1, (int)MathUtils.lerp(0f, len-1, dst));
		final ChunkMesh mesh = meshes[index];
		
		// update mesh
		if (isDirty) {
			isDirty = false;
			build(index, true, true);
		} else {
			if (mesh.isDirty) {
				build(index, true, false);
			}
		}
		
		mesh.render();
	}
	
	protected int getDistIndex(Vector3 camPos) {
		final int haft = (SIZE/2);
		final float dst = camPos.dst((xPos*SIZE)+haft, yCenter, (zPos*SIZE)+haft)/256f;
		return Math.min(len-1, (int)MathUtils.lerp(0f, len-1, dst));
	}

	@Override
	public void dispose() {
		for (int i = 0; i < len; i++)
			meshes[i].dispose();
	}
	
	// Taken from https://github.com/libgdx/libgdx/pull/6086
	protected PlaneSide testBounds(final Plane plane) { 
		final float haft = SIZE/2;
		// Compute the projection interval radius of b onto L(t) = b.c + t * p.n
		final float radius = haft * Math.abs(plane.normal.x) +
							 haftHeight * Math.abs(plane.normal.y) +
							 haft * Math.abs(plane.normal.z);

		// Compute distance of box center from plane
		final float dist = plane.normal.dot((xPos*SIZE)+haft, yCenter, (zPos*SIZE)+haft) + plane.d;

		// Intersection occurs when plane distance falls within [-r,+r] interval
		if (dist > radius) {
			return PlaneSide.Front;
		} else if (dist < -radius) {
			return PlaneSide.Back;
		}
		
		return PlaneSide.OnPlane;
	}
}
