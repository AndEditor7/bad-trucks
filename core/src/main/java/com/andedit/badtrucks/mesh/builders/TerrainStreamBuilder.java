package com.andedit.badtrucks.mesh.builders;

import static com.andedit.badtrucks.mesh.verts.TerrainStream.context;

import java.nio.ByteBuffer;

import com.andedit.badtrucks.chunk.Chunk;
import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.verts.TerrainStream;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class TerrainStreamBuilder extends MeshBuilder
{	
	private static final ByteBuffer BUFFER = BufferUtils.newByteBuffer(((Chunk.SIZE*Chunk.SIZE)*4)*TerrainStream.byteSize);
	
	public void rect(VertInfo v1, VertInfo v2, VertInfo v3, VertInfo v4) {
		vertices.add(v1.x, v1.y, v1.z, v1.d);
		vertices.add(v4.x, v4.y, v4.z, v4.d);
		vertices.add(v3.x, v3.y, v3.z, v3.d);
		vertices.add(v2.x, v2.y, v2.z, v2.d);
	}
	
	@Override
	public ChunkMesh create() {
		return new ChunkMesh(BUFFER, vertices, context, GL20.GL_STATIC_DRAW);
	}
	
	public static class VertInfo
	{
		/** Position */
		public float x, y, z;
		/** Data */
		public float d;
		
		public void set(VertInfo vert) {
			x = vert.x;
			y = vert.y;
			z = vert.z;
			d = vert.d;
		}
	}
}
