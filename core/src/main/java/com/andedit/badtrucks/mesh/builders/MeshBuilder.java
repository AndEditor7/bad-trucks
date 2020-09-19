package com.andedit.badtrucks.mesh.builders;
import com.andedit.badtrucks.mesh.ChunkMesh;
import com.badlogic.gdx.utils.FloatArray;

/** Extendible MeshBuilder. */
public abstract class MeshBuilder
{
	public final FloatArray vertices = new FloatArray(64);
	
	public void begin() {
		vertices.clear();
	}
	
	public abstract ChunkMesh create();
}
