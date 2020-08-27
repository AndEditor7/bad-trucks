package com.andedit.badtrucks.chunk;

import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.builders.TerrainBuilder;
import com.andedit.badtrucks.mesh.builders.TerrainBuilder.VertInfo;
import com.andedit.badtrucks.world.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Chunk implements Disposable
{
	private static final TerrainBuilder build = new TerrainBuilder();
	
	private static final VertInfo 
	v1 = new VertInfo(), 
	v2 = new VertInfo(), 
	v3 = new VertInfo(), 
	v4 = new VertInfo();
	
	/** The size of the tiles. */
	public static final int SIZE = 32;
	
	public static final int LEVELS = 1;
	public static final float[] DATAS = {1.0f, 0.75f, 0.5f}; // TODO: Try replace 0.75f with 1f/1.5f if there something went wrong.
	
	public ChunkMesh mesh;
	
	public final int x, z;
	
	/*
		
	createVertex(vec3(1f, 0f, 1f)); v1
	createVertex(vec3(1f, 0f, 0f)); v2
	createVertex(vec3(0f, 0f, 1f)); v3
	createVertex(vec3(0f, 0f, 0f)); v4
	
		v2----v1
		|      |
		|      |
		v4----v3
	
	*/
	
	public Chunk(World world, int x, int z) {
		this.x = x;
		this.z = z;
		//meshes = new ChunkMesh[LEVELS];
		
		v1.c = v2.c = v3.c = v4.c = Color.LIME.toFloatBits();
		build.begin();
		for (x = 0; x < SIZE; x++) {
			int realX = x+(this.x*SIZE);
		for (z = 0; z < SIZE; z++) {
			int realZ = z+(this.z*SIZE);
			
			v1.y = world.getHeight(realX+1, realZ+1);
			v2.y = world.getHeight(realX+1, realZ);
			v3.y = world.getHeight(realX,   realZ+1);
			v4.y = world.getHeight(realX,   realZ);
			
			v1.x = realX+1;
			v2.x = realX+1;
			v3.x = realX;
			v4.x = realX;
			
			v1.z = realZ+1;
			v2.z = realZ;
			v3.z = realZ+1;
			v4.z = realZ;
			
			build.rect(v1, v2, v3, v4);
		}}
		
		mesh = build.create();
			
	}
	
	/** Upload the vertices to GPU. */
	public void upload() {
		
	}
	
	public void render(Vector3 camPos) {
		mesh.render();
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}
}
