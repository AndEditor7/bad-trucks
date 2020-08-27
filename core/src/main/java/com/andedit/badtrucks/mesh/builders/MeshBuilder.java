package com.andedit.badtrucks.mesh.builders;

import com.andedit.badtrucks.mesh.ChunkMesh;
import com.badlogic.gdx.utils.FloatArray;

/** Extendible MeshBuilder. */
public abstract class MeshBuilder
{
	protected final FloatArray vertexs = new FloatArray(64);
	
	/** Is MeshBuilder building. */
	protected boolean isBuilding;
	
	public void begin() {
		if (isBuilding) return;
		isBuilding = true;
		vertexs.clear();
	}
	
	public abstract ChunkMesh create();
}
