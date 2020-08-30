package com.andedit.badtrucks.mesh.builders;

import com.andedit.badtrucks.glutils.VertContext;
import com.andedit.badtrucks.handles.Shaders;
import com.andedit.badtrucks.mesh.ChunkMesh;
import com.andedit.badtrucks.mesh.verts.TerrainVert;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TerrainBuilder extends MeshBuilder
{
	private static VertContext context = new VertContext() {
		public VertexAttributes getAttrs() {
			return TerrainVert.attributes;
		}
		public ShaderProgram getShader() {
			return Shaders.shader;
		}
		public int getLocation(int i) {
			return Shaders.locations[i];
		}
	};
	
	/*
	v1.pos.set(x+1, y, z);
	v2.pos.set(x, y, z);
	v3.pos.set(x, y, z+1);
	v4.pos.set(x+1, y, z+1);
	
		v1----v4  // new
		|      |
		|      |
		v2----v3
	*/
	public void rect(VertInfo v1, VertInfo v2, VertInfo v3, VertInfo v4) {
		vertexs.add(v1.x, v1.y, v1.z, v1.c);
		vertexs.add(v2.x, v2.y, v2.z, v2.c);
		vertexs.add(v3.x, v3.y, v3.z, v3.c);
		vertexs.add(v4.x, v4.y, v4.z, v4.c);
	}
	
	@Override
	public ChunkMesh create() {
		return new ChunkMesh(vertexs, context);
	}
	
	public static class VertInfo {
		public float x, y, z;
		public float c;
		
		public void set(final VertInfo vert) {
			x = vert.x;
			y = vert.y;
			z = vert.z;
			c = vert.c;
		}
	}
}
