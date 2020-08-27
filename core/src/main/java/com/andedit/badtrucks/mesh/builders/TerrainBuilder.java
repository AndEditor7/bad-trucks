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
	
	createVertex(vec3(1f, 0f, 1f)); v1
	createVertex(vec3(1f, 0f, 0f)); v2
	createVertex(vec3(0f, 0f, 1f)); v3
	createVertex(vec3(0f, 0f, 0f)); v4
	
		v2----v1
		|      |
		|      |
		v4----v3
	
	*/
	public void rect(VertInfo v1, VertInfo v2, VertInfo v3, VertInfo v4) {
		vertexs.add(v1.x, v1.y, v1.z, v1.c);
		vertexs.add(v2.x, v2.y, v2.z, v2.c);
		vertexs.add(v3.x, v3.y, v3.z, v3.c);
		vertexs.add(v4.x, v4.y, v4.z, v4.c);
	}
	
	@Override
	public ChunkMesh create() {
		if (!isBuilding) return null;
		isBuilding = false;
		return new ChunkMesh(vertexs, context);
	}
	
	public static class VertInfo {
		public float x, y, z;
		public float c;
	}
}
