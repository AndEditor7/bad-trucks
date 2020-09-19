package com.andedit.badtrucks.mesh.builders;

import static com.andedit.badtrucks.mesh.verts.TerrainStatic.context;

import java.nio.ByteBuffer;

import com.andedit.badtrucks.chunk.Chunk;
import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.verts.TerrainStatic;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;

public class TerrainStaticBuilder extends MeshBuilder
{	
	private static final ByteBuffer BUFFER = BufferUtils.newByteBuffer(((Chunk.SIZE*Chunk.SIZE)*4)*TerrainStatic.byteSize);
	
	public void rect(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4) {
		vertices.add(v1.x, v1.y, v1.z);
		vertices.add(v4.x, v4.y, v4.z);
		vertices.add(v3.x, v3.y, v3.z);
		vertices.add(v2.x, v2.y, v2.z);
	}
	
	@Override
	public ChunkMesh create() {
		return new ChunkMesh(BUFFER, vertices, context, GL20.GL_STATIC_DRAW);
	}
}
