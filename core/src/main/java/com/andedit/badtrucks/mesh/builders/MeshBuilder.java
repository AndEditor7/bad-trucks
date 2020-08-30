package com.andedit.badtrucks.mesh.builders;

import com.andedit.badtrucks.mesh.ChunkMesh;
import com.badlogic.gdx.utils.FloatArray;

/** Extendible MeshBuilder. */
public abstract class MeshBuilder
{
	protected final FloatArray vertexs = new FloatArray(64);
	
	public void begin() {
		vertexs.clear();
	}
	
	public abstract ChunkMesh create();
}
